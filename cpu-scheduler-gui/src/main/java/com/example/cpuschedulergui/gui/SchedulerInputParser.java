package com.example.cpuschedulergui.gui;

import com.example.cpuschedulergui.algorithms.*;
import com.example.cpuschedulergui.algorithms.Process;

import java.util.*;
import java.util.stream.Collectors;

public class SchedulerInputParser {
    public enum PriorityOrder {
        ASCENDING,
        DESCENDING
    }

    public static FCFSCpuScheduler parseFCFS(String content) {
        Map<String, String> fields = parseFields(content);

        List<Integer> pids = parseIntList(requiredField(fields, "pid"));
        List<Integer> arrivalTimes = parseIntList(requiredField(fields, "time arrive"));
        List<Integer> burstTimes = parseIntList(requiredField(fields, "burst time"));

        validateSameSize(pids, arrivalTimes, burstTimes);

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < pids.size(); ++i) {
            processes.add(new Process(arrivalTimes.get(i), burstTimes.get(i), pids.get(i)));
        }

        return new FCFSCpuScheduler(processes);
    }

    public static NonpreemSJFCpuScheduler parseNonpreemSJF(String content) {
        Map<String, String> fields = parseFields(content);

        List<Integer> pids = parseIntList(requiredField(fields, "pid"));
        List<Integer> arrivalTimes = parseIntList(requiredField(fields, "time arrive"));
        List<Integer> burstTimes = parseIntList(requiredField(fields, "burst time"));

        validateSameSize(pids, arrivalTimes, burstTimes);

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < pids.size(); ++i) {
            processes.add(new Process(arrivalTimes.get(i), burstTimes.get(i), pids.get(i)));
        }

        return new NonpreemSJFCpuScheduler(processes);
    }

    public static PreemSJFCpuScheduler parsePreemSJF(String content) {
        Map<String, String> fields = parseFields(content);

        List<Integer> pids = parseIntList(requiredField(fields, "pid"));
        List<Integer> arrivalTimes = parseIntList(requiredField(fields, "time arrive"));
        List<Integer> burstTimes = parseIntList(requiredField(fields, "burst time"));

        validateSameSize(pids, arrivalTimes, burstTimes);

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < pids.size(); ++i) {
            processes.add(new Process(arrivalTimes.get(i), burstTimes.get(i), pids.get(i)));
        }

        return new PreemSJFCpuScheduler(processes);
    }

    public static RoundRobinCpuScheduler parseRoundRobin(String content) {
        Map<String, String> fields = parseFields(content);

        int timeQuantum = Integer.parseInt(requiredField(fields, "time quantum"));
        List<Integer> pids = parseIntList(requiredField(fields, "pid"));
        List<Integer> arrivalTimes = parseIntList(requiredField(fields, "time arrive"));
        List<Integer> burstTimes = parseIntList(requiredField(fields, "burst time"));

        validateSameSize(pids, arrivalTimes, burstTimes);

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < pids.size(); ++i) {
            processes.add(new Process(arrivalTimes.get(i), burstTimes.get(i), pids.get(i)));
        }

        return new RoundRobinCpuScheduler(processes, timeQuantum);
    }

    public static NonpreemPriorityCpuScheduler parseNonpreemPriority(String content) {
        Map<String, String> fields = parseFields(content);

        String orderValue = requiredField(fields, "order").trim().toLowerCase();

        List<Integer> pids = parseIntList(requiredField(fields, "pid"));
        List<Integer> arrivalTimes = parseIntList(requiredField(fields, "time arrive"));
        List<Integer> priorities = parseIntList(requiredField(fields, "priority"));
        List<Integer> burstTimes = parseIntList(requiredField(fields, "burst time"));

        validateSameSize(pids, arrivalTimes, priorities, burstTimes);

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < pids.size(); ++i) {
            processes.add(new Process(arrivalTimes.get(i), burstTimes.get(i), priorities.get(i), pids.get(i)));
        }

        return new NonpreemPriorityCpuScheduler(processes, (orderValue.equals("ascending") ? NonpreemPriorityCpuScheduler.ASCENDING : NonpreemPriorityCpuScheduler.DESCENDING));
    }

    public static PreemPriorityCpuScheduler parsePreemPriority(String content) {
        Map<String, String> fields = parseFields(content);

        String orderValue = requiredField(fields, "order").trim().toLowerCase();

        List<Integer> pids = parseIntList(requiredField(fields, "pid"));
        List<Integer> arrivalTimes = parseIntList(requiredField(fields, "time arrive"));
        List<Integer> priorities = parseIntList(requiredField(fields, "priority"));
        List<Integer> burstTimes = parseIntList(requiredField(fields, "burst time"));

        validateSameSize(pids, arrivalTimes, priorities, burstTimes);

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < pids.size(); ++i) {
            processes.add(new Process(arrivalTimes.get(i), burstTimes.get(i), priorities.get(i), pids.get(i)));
        }

        return new PreemPriorityCpuScheduler(processes, (orderValue.equals("ascending") ? PreemPriorityCpuScheduler.ASCENDING : PreemPriorityCpuScheduler.DESCENDING));
    }

    public static MultilevelQueueCpuScheduler parseMultilevel(String content) {
        Map<String, String> fields = parseFields(content);

        List<MultilevelQueueCpuScheduler.CpuSchedulerOption> cpuSchedulerOptions =
                parseCpuSchedulerOptions(requiredField(fields, "cpu scheduler type"));

        List<Integer> pids = parseIntList(requiredField(fields, "pid"));
        List<Integer> arrivalTimes = parseIntList(requiredField(fields, "time arrive"));
        List<Integer> priorities = parseIntList(requiredField(fields, "priority"));
        List<Integer> burstTimes = parseIntList(requiredField(fields, "burst time"));
        List<Integer> queueOrders = parseIntList(requiredField(fields, "queue order"));

        validateSameSize(pids, arrivalTimes, priorities, burstTimes, queueOrders);

        int maxQueueOrder = queueOrders.stream()
                .max(Integer::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("queue order is empty"));

        List<List<Process>> processes = new ArrayList<>();
        for (int i = 0; i <= maxQueueOrder; ++i) {
            processes.add(new ArrayList<>());
        }

        for (int i = 0; i < pids.size(); ++i) {
            int queueIndex = queueOrders.get(i);

            if (queueIndex < 0 || queueIndex >= processes.size()) {
                throw new IllegalArgumentException("Invalid queue order: " + queueIndex);
            }

            Process process = new Process(arrivalTimes.get(i), burstTimes.get(i), priorities.get(i), pids.get(i));

            processes.get(queueIndex).add(process);
        }

        if (cpuSchedulerOptions.size() != processes.size()) {
            throw new IllegalArgumentException(
                    "Number of cpu scheduler options (" + cpuSchedulerOptions.size() +
                            ") must match number of queues (" + processes.size() + ")"
            );
        }

        return new MultilevelQueueCpuScheduler(processes, cpuSchedulerOptions);
    }

    private static List<MultilevelQueueCpuScheduler.CpuSchedulerOption> parseCpuSchedulerOptions(String value) {
            List<String> schedulerSpecs = splitBySemicolon(value);
        List<MultilevelQueueCpuScheduler.CpuSchedulerOption> options = new ArrayList<>();

        for (String spec : schedulerSpecs) {
            List<String> parts = splitByComma(spec);

            if (parts.isEmpty()) {
                throw new IllegalArgumentException("Empty cpu scheduler option");
            }

            int option = Integer.parseInt(normalizeToken(parts.get(0)));

            switch (option) {
                case MultilevelQueueCpuScheduler.FCFS_CPU_SCHEDULER,
                     MultilevelQueueCpuScheduler.NONPREEMPTIVE_SJF_CPU_SCHEDULER,
                     MultilevelQueueCpuScheduler.PREEMPTIVE_SJF_CPU_SCHEDULER -> {
                    if (parts.size() != 1) {
                        throw new IllegalArgumentException("Scheduler option " + option + " does not accept extra parameters");
                    }
                    options.add(new MultilevelQueueCpuScheduler.CpuSchedulerOption(option));
                }

                case MultilevelQueueCpuScheduler.NONPREEMPTIVE_PRIORITY_CPU_SCHEDULER,
                     MultilevelQueueCpuScheduler.PREEMPTIVE_PRIORITY_CPU_SCHEDULER -> {
                    if (parts.size() != 2) {
                        throw new IllegalArgumentException("Priority scheduler option " + option + " requires ascending/descending");
                    }

                    String orderToken = normalizeToken(parts.get(1));
                    boolean ascending = switch (orderToken) {
                        case "ascending" -> true;
                        case "descending" -> false;
                        default -> throw new IllegalArgumentException("Invalid priority order: " + orderToken);
                    };

                    options.add(new MultilevelQueueCpuScheduler.CpuSchedulerOption(option, ascending));
                }

                case MultilevelQueueCpuScheduler.PREEMPTIVE_ROUND_ROBIN_SCHEDULER -> {
                    if (parts.size() != 2) {
                        throw new IllegalArgumentException("Round robin scheduler option requires time quantum");
                    }

                    int timeQuantum = Integer.parseInt(normalizeToken(parts.get(1)));
                    options.add(new MultilevelQueueCpuScheduler.CpuSchedulerOption(option, timeQuantum));
                }

                default -> throw new IllegalArgumentException("Unknown scheduler option: " + option);
            }
        }

        return options;
    }

    private static List<String> splitBySemicolon(String value) {
        return Arrays.stream(value.split("\\s*;\\s*"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private static List<String> splitByComma(String value) {
        return Arrays.stream(value.split("\\s*,\\s*"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private static String normalizeToken(String token) {
        String result = token.trim();
        if (result.startsWith("\"") && result.endsWith("\"") && result.length() >= 2) {
            result = result.substring(1, result.length() - 1).trim();
        }
        return result.toLowerCase();
    }

    private static Map<String, String> parseFields(String content) {
        Map<String, String> fields = new LinkedHashMap<>();

        String[] lines = content.split("\\R");
        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }

            int colonIndex = line.indexOf(':');
            if (colonIndex < 0) {
                throw new IllegalArgumentException("Invalid line (missing ':'): " + line);
            }

            String key = line.substring(0, colonIndex).trim().toLowerCase();
            String value = line.substring(colonIndex + 1).trim();
            fields.put(key, value);
        }

        return fields;
    }

    private static String requiredField(Map<String, String> fields, String key) {
        String value = fields.get(key.toLowerCase());
        if (value == null) {
            throw new IllegalArgumentException("Missing field: " + key);
        }
        return value;
    }

    private static List<String> parseStringList(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private static List<Integer> parseIntList(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    @SafeVarargs
    private static void validateSameSize(List<?>... lists) {
        if (lists.length == 0) {
            return;
        }

        int size = lists[0].size();
        for (List<?> list : lists) {
            if (list.size() != size) {
                throw new IllegalArgumentException("Input lists do not have the same size");
            }
        }
    }
}