package com.example.cpuschedulergui.gui;

public class SchedulerResult {
    private final String ganttChart;
    private final int totalTurnaroundTime;
    private final double averageTurnaroundTime;
    private final int totalWaitingTime;
    private final double averageWaitingTime;
    private final int totalResponseTime;
    private final double averageResponseTime;
    private final int numberOfContextSwitches;

    public SchedulerResult(
            String ganttChart,
            int sumTurnaroundTime,
            double averageTurnaroundTime,
            int sumWaitingTime,
            double averageWaitingTime,
            int sumResponseTime,
            double averageResponseTime,
            int numberOfContextSwitches
    ) {
        this.ganttChart = ganttChart;
        this.totalTurnaroundTime = sumTurnaroundTime;
        this.averageTurnaroundTime = averageTurnaroundTime;
        this.totalWaitingTime = sumWaitingTime;
        this.averageWaitingTime = averageWaitingTime;
        this.totalResponseTime = sumResponseTime;
        this.averageResponseTime = averageResponseTime;
        this.numberOfContextSwitches = numberOfContextSwitches;
    }

    public String getGanttChart() {
        return ganttChart;
    }

    public int getTotalTurnaroundTime() {
        return totalTurnaroundTime;
    }

    public double getAverageTurnaroundTime() {
        return averageTurnaroundTime;
    }

    public int getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public int getTotalResponseTime() {
        return totalResponseTime;
    }

    public double getAverageResponseTime() {
        return averageResponseTime;
    }

    public int getNumberOfContextSwitches() {
        return numberOfContextSwitches;
    }
}