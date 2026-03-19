package com.example.cpuschedulergui.algorithms;

import java.util.*;

public class PreemPriorityCpuScheduler extends CpuScheduler {
    public static final boolean ASCENDING = true;
    public static final boolean DESCENDING = false;

    private final Queue<Process> processQueue;
    private final PriorityQueue<Process> readyQueue;
    private final Comparator<Process> processComparator;
    private final int factor;

    public PreemPriorityCpuScheduler(List<Process> processes, boolean ascending) {
        super(processes);

        factor = (ascending ? 1 : -1);

        processComparator = (lhs, rhs) -> {
            if (lhs.getPriority() != rhs.getPriority()) {
                return (factor * lhs.getPriority() < factor * rhs.getPriority() ? -1 : 1);
            }
            if (lhs.getPid() != rhs.getPid()) {
                return (lhs.getPid() < rhs.getPid() ? -1 : 1);
            }
            return 0;
        };

        processes.sort((lhs, rhs) -> {
            if (lhs.getArriveTime() != rhs.getArriveTime()) {
                return (lhs.getArriveTime() < rhs.getArriveTime() ? -1 : 1);
            }
            if (lhs.getPriority() != rhs.getPriority()) {
                return (factor * lhs.getPriority() < factor * rhs.getPriority() ? -1 : 1);
            }
            if (lhs.getPid() != rhs.getPid()) {
                return (lhs.getPid() < rhs.getPid() ? -1 : 1);
            }
            return 0;
        });

        processQueue = new LinkedList<>();
        processQueue.addAll(processes);

        readyQueue = new PriorityQueue<>(processComparator);
    }

    @Override
    public void runCpuScheduler() {
        while (!processQueue.isEmpty() || !readyQueue.isEmpty()) {
            while (!processQueue.isEmpty() && getGlobalEndTime() >= processQueue.peek().getArriveTime()) {
                // This case only happened when I applied the multilevel algorithm (maybe)
                readyQueue.add(processQueue.poll());
            }
            if (readyQueue.isEmpty()) {
                // Why only add one?
                // This is because I sorted by pid above (if equal coming time and priority)
                readyQueue.add(processQueue.poll());
            }

            Process process = readyQueue.poll();
            int startTime = Math.max(getGlobalEndTime(), process.getArriveTime());
            int endTime = startTime + process.getExecutionTime();

            while (!processQueue.isEmpty() && processQueue.peek().getArriveTime() < endTime) {
                Process newProcess = processQueue.poll();
                readyQueue.add(newProcess);

                assert (newProcess.getArriveTime() >= startTime);
                int executedTime = newProcess.getArriveTime() - startTime;

                if (factor * newProcess.getPriority() < factor * process.getPriority()) {
                    endTime = startTime + executedTime;
                    break;
                }
            }
            process.setExecutionTime(process.getExecutionTime() - (endTime - startTime));
            CpuScheduler.Event event = new CpuScheduler.Event(startTime, endTime, process.getPid());
            addEvent(event, process);

            if (process.getExecutionTime() > 0) {
                readyQueue.add(process);
            }
        }
    }
}
