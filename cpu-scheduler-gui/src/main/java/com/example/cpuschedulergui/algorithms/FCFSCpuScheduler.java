package com.example.cpuschedulergui.algorithms;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FCFSCpuScheduler extends NonpreemptiveCpuScheduler {
    private Queue<Process> readyQueue;

    public FCFSCpuScheduler(List<Process> processes) {
        super(processes);

        processes.sort((lhs, rhs) -> {
            if (lhs.getArriveTime() != rhs.getArriveTime()) {
                return (lhs.getArriveTime() < rhs.getArriveTime() ? -1 : 1);
            }
            if (lhs.getPid() != rhs.getPid()) {
                return (lhs.getPid() < rhs.getPid() ? -1 : 1);
            }
            return 0;
        });
        readyQueue = new LinkedList<>();
        readyQueue.addAll(processes);
    }

    @Override
    public void runCpuScheduler() {
        while (!readyQueue.isEmpty()) {
            Process process = readyQueue.poll();
            addProcessToCPU(process);
        }
    }
}
