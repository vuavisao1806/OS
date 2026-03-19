package com.example.cpuschedulergui.algorithms;

import java.util.*;

public class NonpreemSJFCpuScheduler extends NonpreemptiveCpuScheduler {
    private final Queue<Process> processQueue;
    private final PriorityQueue<Process> readyQueue;
    private final Comparator<Process> processComparator;

    public NonpreemSJFCpuScheduler(List<Process> processes) {
        super(processes);

        processComparator = (lhs, rhs) -> {
            if (lhs.getExecutionTime() != rhs.getExecutionTime()) {
                return (lhs.getExecutionTime() < rhs.getExecutionTime() ? -1 : 1);
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
            if (lhs.getExecutionTime() != rhs.getExecutionTime()) {
                return (lhs.getExecutionTime() < rhs.getExecutionTime() ? -1 : 1);
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
                // This is because I sorted by pid above (if equal coming time and CPU burst)
                readyQueue.add(processQueue.poll());
            }
            Process process = readyQueue.poll();
            addProcessToCPU(process);
        }
    }
}
