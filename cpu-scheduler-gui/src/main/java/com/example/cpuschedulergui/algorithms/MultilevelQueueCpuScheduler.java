package com.example.cpuschedulergui.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultilevelQueueCpuScheduler extends CpuScheduler {
    public static final int FCFS_CPU_SCHEDULER = 0;
    public static final int NONPREEMPTIVE_SJF_CPU_SCHEDULER = 1;
    public static final int NONPREEMPTIVE_PRIORITY_CPU_SCHEDULER = 2;
    public static final int PREEMPTIVE_SJF_CPU_SCHEDULER = 3;
    public static final int PREEMPTIVE_PRIORITY_CPU_SCHEDULER = 4;
    public static final int PREEMPTIVE_ROUND_ROBIN_SCHEDULER = 5;

    public static class CpuSchedulerOption {
        int option;
        boolean ascending;
        int timeQuantum;

        public CpuSchedulerOption(int option) {
            this.option = option;
        }

        public CpuSchedulerOption(int option, boolean ascending) {
            this.option = option;
            this.ascending = ascending;
        }

        public CpuSchedulerOption(int option, int timeQuantum) {
            this.option = option;
            this.timeQuantum = timeQuantum;
        }
    }

    private final List<CpuScheduler> cpuSchedulers;

    public MultilevelQueueCpuScheduler(List<List<Process>> processes, List<CpuSchedulerOption> schedulerTypes) {
        super(Collections.emptyList());

        assert (processes.size() == schedulerTypes.size());
        cpuSchedulers = new ArrayList<>();

        int numberOfProcess = 0;
        for (int i = 0; i < processes.size(); ++i) {
            int option = schedulerTypes.get(i).option;
            List<Process> currentProcess = new ArrayList<>(processes.get(i));
            numberOfProcess += currentProcess.size();

            CpuScheduler cpuScheduler;
            switch (option) {
                case FCFS_CPU_SCHEDULER -> cpuScheduler = new FCFSCpuScheduler(currentProcess);
                case NONPREEMPTIVE_SJF_CPU_SCHEDULER -> cpuScheduler = new NonpreemSJFCpuScheduler(currentProcess);
                case NONPREEMPTIVE_PRIORITY_CPU_SCHEDULER -> cpuScheduler = new NonpreemPriorityCpuScheduler(currentProcess, schedulerTypes.get(i).ascending);
                case PREEMPTIVE_SJF_CPU_SCHEDULER -> cpuScheduler = new PreemSJFCpuScheduler(currentProcess);
                case PREEMPTIVE_PRIORITY_CPU_SCHEDULER -> cpuScheduler = new PreemPriorityCpuScheduler(currentProcess, schedulerTypes.get(i).ascending);
                case PREEMPTIVE_ROUND_ROBIN_SCHEDULER -> cpuScheduler = new RoundRobinCpuScheduler(currentProcess,  schedulerTypes.get(i).timeQuantum);
                default -> throw new RuntimeException("The CPU scheduler is not implemented yet");
            }
            cpuSchedulers.add(cpuScheduler);
        }
        setNumberOfProcess(numberOfProcess);
    }

    @Override
    public void runCpuScheduler() {
        for (int i = 0; i < cpuSchedulers.size(); ++i) {
            CpuScheduler cpuScheduler = cpuSchedulers.get(i);
            if (i >= 1) {
                CpuScheduler lastCpuScheduler = cpuSchedulers.get(i - 1);
                cpuScheduler.setEvents(lastCpuScheduler.getEvents());
                cpuScheduler.setSumTurnaroundTime(lastCpuScheduler.getSumTurnaroundTime());
                cpuScheduler.setSumWaitingTime(lastCpuScheduler.getSumWaitingTime());
                cpuScheduler.setSumResponseTime(lastCpuScheduler.getSumResponseTime());
                cpuScheduler.setNumberOfContextSwitches(lastCpuScheduler.getNumberOfContextSwitches());
            }
            cpuScheduler.runCpuScheduler();
        }
        CpuScheduler lastCpuScheduler = cpuSchedulers.getLast();
        setEvents(lastCpuScheduler.getEvents());
        setSumTurnaroundTime(lastCpuScheduler.getSumTurnaroundTime());
        setSumWaitingTime(lastCpuScheduler.getSumWaitingTime());
        setSumResponseTime(lastCpuScheduler.getSumResponseTime());
        setNumberOfContextSwitches(lastCpuScheduler.getNumberOfContextSwitches());
    }
}