package com.example.cpuschedulergui.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CpuScheduler {
    protected static class Event {
        private final int startTime;
        private final int endTime;
        private final int processPid;

        public Event(int startTime, int endTime, int processPid) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.processPid = processPid;
        }

        public int getDuration() {
            return endTime - startTime;
        }

        public int getStartTime() {
            return startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public int getProcessPid() {
            return processPid;
        }

        @Override
        public String toString() {
            return String.format("P%d: %d -> %d\n", processPid, startTime, endTime);
        }
    }

    private List<Event> events;
    private Map<Integer, Integer> lastTimeOnReadyQueue;

    private int numberOfProcess;
    private int sumTurnaroundTime;
    private int sumWaitingTime;
    private int sumResponseTime;
    private int numberOfContextSwitches;


    public CpuScheduler(List<Process> processes) {
        events = new ArrayList<>();
        lastTimeOnReadyQueue = new HashMap<>();
        sumTurnaroundTime = 0;
        sumWaitingTime = 0;
        sumResponseTime = 0;
        numberOfContextSwitches = 0;
        numberOfProcess = processes.size();

        for (Process process : processes) {
           lastTimeOnReadyQueue.put(process.getPid(), process.getArriveTime());
        }
    }

    public abstract void runCpuScheduler();

    protected final void addEvent(Event event, Process process) {
        sumWaitingTime += event.startTime - lastTimeOnReadyQueue.get(event.processPid);
        if (process.getArriveTime() == lastTimeOnReadyQueue.get(process.getPid())) {
            sumResponseTime += event.startTime - process.getArriveTime();
        }
        if (process.getExecutionTime() == 0) {
            sumTurnaroundTime += event.endTime - process.getArriveTime();
        } else {
            ++numberOfContextSwitches;
        }
        lastTimeOnReadyQueue.put(event.processPid, event.endTime);

        events.add(event);
    }

    public List<Event> getEvents() {
        return events;
    }

    public int getGlobalEndTime() {
        return (events.isEmpty() ? 0 : events.getLast().endTime);
    }

    public int getSumTurnaroundTime() {
        return sumTurnaroundTime;
    }

    public int getSumWaitingTime() {
        return sumWaitingTime;
    }

    public int getSumResponseTime() {
        return sumResponseTime;
    }

    public double getAverageTurnaroundTime() {
        return 1.0 * sumTurnaroundTime / numberOfProcess;
    }

    public double getAverageWaitingTime() {
        return 1.0 * sumWaitingTime / numberOfProcess;
    }

    public double getAverageResponseTime() {
        return 1.0 * sumResponseTime / numberOfProcess;
    }

    public int getNumberOfContextSwitches() {
        return numberOfContextSwitches;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setSumTurnaroundTime(int sumTurnaroundTime) {
        this.sumTurnaroundTime = sumTurnaroundTime;
    }

    public void setSumWaitingTime(int sumWaitingTime) {
        this.sumWaitingTime = sumWaitingTime;
    }

    public void setSumResponseTime(int sumResponseTime) {
        this.sumResponseTime = sumResponseTime;
    }

    public void setNumberOfContextSwitches(int numberOfContextSwitches) {
        this.numberOfContextSwitches = numberOfContextSwitches;
    }

    public void setNumberOfProcess(int numberOfProcess) {
        this.numberOfProcess = numberOfProcess;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Total turnaround time: %d\n", sumTurnaroundTime));
        stringBuilder.append(String.format("Average turnaround time: %.2f\n", getAverageTurnaroundTime()));
        stringBuilder.append(String.format("Total waiting time: %d\n", sumWaitingTime));
        stringBuilder.append(String.format("Average waiting time: %.2f\n", getAverageWaitingTime()));
        stringBuilder.append(String.format("Total response time: %d\n", sumResponseTime));
        stringBuilder.append(String.format("Average response time: %.2f\n", getAverageResponseTime()));
        stringBuilder.append(String.format("The number of context switches: %d\n", numberOfContextSwitches));
        for (Event event: events) {
            stringBuilder.append(event.toString());
        }
        return stringBuilder.toString();
    }
}
