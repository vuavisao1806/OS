package com.example.cpuschedulergui.algorithms;

public class Process {
    private final int arriveTime;
    private int executionTime;
    private final int priority;
    private final int pid;

    public Process(int executionTime, int pid) {
        this.arriveTime = 0;
        this.executionTime = executionTime;
        this.pid = pid;
        this.priority = 0;
    }

    public Process(int arriveTime, int executionTime, int pid) {
        this.arriveTime = arriveTime;
        this.executionTime = executionTime;
        this.pid = pid;
        this.priority = 0;
    }

    public Process(int arriveTime, int executionTime, int priority, int pid) {
        this.arriveTime = arriveTime;
        this.executionTime = executionTime;
        this.priority = priority;
        this.pid = pid;
    }

    public int getPriority() {
        return priority;
    }

    public int getArriveTime() {
        return arriveTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public int getPid() {
        return pid;
    }
}
