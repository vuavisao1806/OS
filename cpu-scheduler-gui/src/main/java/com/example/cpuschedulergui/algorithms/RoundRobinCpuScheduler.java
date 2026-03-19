package com.example.cpuschedulergui.algorithms;

import java.util.*;

public class RoundRobinCpuScheduler extends CpuScheduler {
    private final Queue<Process> processQueue;
    private final Deque<Process> readyQueue;

    private final int timeQuantum;

    public RoundRobinCpuScheduler(List<Process> processes, int timeQuantum) {
        super(processes);

        this.timeQuantum = timeQuantum;

        processes.sort((lhs, rhs) -> {
            if (lhs.getArriveTime() != rhs.getArriveTime()) {
                return (lhs.getArriveTime() < rhs.getArriveTime() ? -1 : 1);
            }
            if (lhs.getPid() != rhs.getPid()) {
                return (lhs.getPid() < rhs.getPid() ? -1 : 1);
            }
            return 0;
        });

        processQueue = new LinkedList<>();
        processQueue.addAll(processes);

        readyQueue = new ArrayDeque<>();
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
                // This is because I sorted by pid above (if equal coming time)
                readyQueue.addFirst(processQueue.poll());
            }

            Process process = readyQueue.removeFirst();
            int startTime = Math.max(getGlobalEndTime(), process.getArriveTime());
            int endTime = startTime + Math.min(process.getExecutionTime(), timeQuantum);

            process.setExecutionTime(process.getExecutionTime() - (endTime - startTime));
            Event event = new Event(startTime, endTime, process.getPid());
            addEvent(event, process);

            // I'm not sure about it
//            while (!processQueue.isEmpty() && processQueue.peek().getArriveTime() < endTime) {
//                readyQueue.addLast(processQueue.poll());
//            }
//            while (!processQueue.isEmpty()
//                    && processQueue.peek().getArriveTime() == endTime && processQueue.peek().getPid() < process.getPid()) {
//                readyQueue.addLast(processQueue.poll());
//            }
//            if (process.getExecutionTime() > 0) {
//                readyQueue.addLast(process);
//            }
//            while (!processQueue.isEmpty() && processQueue.peek().getArriveTime() == endTime) {
//                readyQueue.addLast(processQueue.poll());
//            }
            while (!processQueue.isEmpty() && processQueue.peek().getArriveTime() <= endTime) {
                readyQueue.addLast(processQueue.poll());
            }
            if (process.getExecutionTime() > 0) {
                readyQueue.addLast(process);
            }
        }
    }
}
