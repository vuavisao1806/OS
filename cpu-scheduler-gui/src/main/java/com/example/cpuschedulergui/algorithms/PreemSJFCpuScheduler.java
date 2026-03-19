package com.example.cpuschedulergui.algorithms;

import java.util.*;

public class PreemSJFCpuScheduler extends CpuScheduler {
    private final Queue<Process> processQueue;
    private final PriorityQueue<Process> readyQueue;
    private final Comparator<Process> processComparator;

    public PreemSJFCpuScheduler(List<Process> processes) {
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
            // This case will never happen, it was handled below
//            while (!processQueue.isEmpty() && getGlobalEndTime() >= processQueue.peek().getArriveTime()) {
//                readyQueue.add(processQueue.poll());
//            }
            while (!processQueue.isEmpty() && getGlobalEndTime() >= processQueue.peek().getArriveTime()) {
                // This case only happened when I applied the multilevel algorithm (maybe)
                readyQueue.add(processQueue.poll());
            }
            if (readyQueue.isEmpty()) {
                // Why only add one?
                // This is because I sorted by pid above (if equal coming time and CPU burst)
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

                if (process.getExecutionTime() - executedTime > newProcess.getExecutionTime()) {
                    endTime = startTime + executedTime;
                    break;
                }
            }
            process.setExecutionTime(process.getExecutionTime() - (endTime - startTime));
            Event event = new Event(startTime, endTime, process.getPid());
            addEvent(event, process);
//            System.out.println(event);

            if (process.getExecutionTime() > 0) {
                readyQueue.add(process);
            }
        }
    }
}
