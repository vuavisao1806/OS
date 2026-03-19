package algorithms;

import com.example.cpuschedulergui.algorithms.PreemSJFCpuScheduler;
import com.example.cpuschedulergui.algorithms.Process;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PreemSJFCpuSchedulerTest {
    @Test
    void simpleTest() {
        // example in class
        Process p1 = new Process(0, 8, 1);
        Process p2 = new Process(1, 4, 2);
        Process p3 = new Process(2, 9, 3);
        Process p4 = new Process(3, 5, 4);
        List<Process> processes = new ArrayList<>(List.of(p1, p2, p3, p4));
        PreemSJFCpuScheduler preemSJFCpuScheduler = new PreemSJFCpuScheduler(processes);
        preemSJFCpuScheduler.runCpuScheduler();

        assertEquals(52, preemSJFCpuScheduler.getSumTurnaroundTime());
        assertEquals(26, preemSJFCpuScheduler.getSumWaitingTime());
        assertEquals(17, preemSJFCpuScheduler.getSumResponseTime());

        assertEquals(13, preemSJFCpuScheduler.getAverageTurnaroundTime(), 1E-6);
        assertEquals(6.5, preemSJFCpuScheduler.getAverageWaitingTime(), 1E-6);
        assertEquals(4.25, preemSJFCpuScheduler.getAverageResponseTime(), 1E-6);
    }

    @Test
    void simpleTest2() {
        // example in Nonpreemptive SJF (in class)

        Process p1 = new Process(6, 1);
        Process p2 = new Process(8, 2);
        Process p3 = new Process(7, 3);
        Process p4 = new Process(3, 4);
        List<Process> processes = new ArrayList<>(List.of(p1, p2, p3, p4));

        PreemSJFCpuScheduler preemSJFCpuScheduler = new PreemSJFCpuScheduler(processes);
        preemSJFCpuScheduler.runCpuScheduler();
        assertEquals(52, preemSJFCpuScheduler.getSumTurnaroundTime());
        assertEquals(28, preemSJFCpuScheduler.getSumWaitingTime());
        assertEquals(28, preemSJFCpuScheduler.getSumResponseTime());

        assertEquals(13, preemSJFCpuScheduler.getAverageTurnaroundTime(), 1E-6);
        assertEquals(7, preemSJFCpuScheduler.getAverageWaitingTime(), 1E-6);
        assertEquals(7, preemSJFCpuScheduler.getAverageResponseTime(), 1E-6);
    }

    @Test
    void test2() {
        // example in https://www.youtube.com/watch?v=qpgXKIsqJGY&t=11s
        // TODO: need verified
        Process p1 = new Process(2, 6, 1);
        Process p2 = new Process(5, 2, 2);
        Process p3 = new Process(1, 8, 3);
        Process p4 = new Process(0, 3, 4);
        Process p5 = new Process(4, 4, 5);
        List<Process> processes = new ArrayList<>(List.of(p1, p2, p3, p4, p5));
        PreemSJFCpuScheduler preemSJFCpuScheduler = new PreemSJFCpuScheduler(processes);
        preemSJFCpuScheduler.runCpuScheduler();

        assertEquals(46, preemSJFCpuScheduler.getSumTurnaroundTime());
        assertEquals(23, preemSJFCpuScheduler.getSumWaitingTime());
        assertEquals(15, preemSJFCpuScheduler.getSumResponseTime());

        assertEquals(9.2, preemSJFCpuScheduler.getAverageTurnaroundTime(), 1E-6);
        assertEquals(4.6, preemSJFCpuScheduler.getAverageWaitingTime(), 1E-6);
        assertEquals(3, preemSJFCpuScheduler.getAverageResponseTime(), 1E-6);
    }
}
