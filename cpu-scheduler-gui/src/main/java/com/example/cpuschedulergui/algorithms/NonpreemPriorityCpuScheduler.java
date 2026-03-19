package com.example.cpuschedulergui.algorithms;

import java.util.*;

public class NonpreemPriorityCpuScheduler extends NonpreemptiveCpuScheduler {
    public static final boolean ASCENDING = true;
    public static final boolean DESCENDING = false;

    private final Queue<Process> processQueue;
    private final PriorityQueue<Process> readyQueue;
    private final Comparator<Process> processComparator;
    private final int factor;

    public NonpreemPriorityCpuScheduler(List<Process> processes, boolean ascending) {
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
                readyQueue.add(processQueue.poll());
            }
            if (readyQueue.isEmpty()) {
                // Why only add one?
                // This is because I sorted by pid above (if equal coming time and priority)
                readyQueue.add(processQueue.poll());
            }
            Process process = readyQueue.poll();
            addProcessToCPU(process);
        }
    }
}
