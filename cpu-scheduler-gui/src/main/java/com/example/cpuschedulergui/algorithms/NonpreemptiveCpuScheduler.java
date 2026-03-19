package com.example.cpuschedulergui.algorithms;

import java.util.List;

public abstract class NonpreemptiveCpuScheduler extends CpuScheduler {

    public NonpreemptiveCpuScheduler(List<Process> processes) {
        super(processes);
    }

    protected final void addProcessToCPU(Process process) {
        int startTime = getGlobalEndTime();
        if (startTime < process.getArriveTime()) {
            startTime = process.getArriveTime();
        }
        int executeTime = process.getExecutionTime();
        int endTime = startTime + executeTime;

        process.setExecutionTime(0);
        Event event = new Event(startTime, endTime, process.getPid());
        addEvent(event, process);
    }
}
