package algorithms;

import com.example.cpuschedulergui.algorithms.PreemPriorityCpuScheduler;
import com.example.cpuschedulergui.algorithms.Process;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PreemPriorityCpuSchedulerTest {
    @Test
    void simpleTest() {
        // page 212 chapter 5 OS
        Process p1 = new Process(0, 10, 3,1);
        Process p2 = new Process(0, 1, 1,2);
        Process p3 = new Process(0, 2, 4, 3);
        Process p4 = new Process(0, 1, 5, 4);
        Process p5 = new Process(0, 5, 2, 5);
        List<Process> processes = new ArrayList<>(List.of(p1, p2, p3, p4, p5));

        PreemPriorityCpuScheduler preemPriorityCpuScheduler = new PreemPriorityCpuScheduler(processes, PreemPriorityCpuScheduler.ASCENDING);
        preemPriorityCpuScheduler.runCpuScheduler();

        assertEquals(60, preemPriorityCpuScheduler.getSumTurnaroundTime());
        assertEquals(41, preemPriorityCpuScheduler.getSumWaitingTime());
        assertEquals(41, preemPriorityCpuScheduler.getSumResponseTime());

        assertEquals(12, preemPriorityCpuScheduler.getAverageTurnaroundTime(), 1E-6);
        assertEquals(8.2, preemPriorityCpuScheduler.getAverageWaitingTime(), 1E-6);
        assertEquals(8.2, preemPriorityCpuScheduler.getAverageResponseTime(), 1E-6);
    }

    @Test
    void test2() {
        // TODO: need verified
        Process p1 = new Process(0, 3, 3, 1);
        Process p2 = new Process(1, 4, 2,2);
        Process p3 = new Process(2, 6, 4, 3);
        Process p4 = new Process(3, 4, 6, 4);
        Process p5 = new Process(5, 2, 10, 5);
        List<Process> processes = new ArrayList<>(List.of(p1, p2, p3, p4, p5));

        PreemPriorityCpuScheduler preemPriorityCpuScheduler = new PreemPriorityCpuScheduler(processes, PreemPriorityCpuScheduler.ASCENDING);
        preemPriorityCpuScheduler.runCpuScheduler();

        assertEquals(50, preemPriorityCpuScheduler.getSumTurnaroundTime());
        assertEquals(31, preemPriorityCpuScheduler.getSumWaitingTime());
        assertEquals(27, preemPriorityCpuScheduler.getSumResponseTime());

        assertEquals(10, preemPriorityCpuScheduler.getAverageTurnaroundTime(), 1E-6);
        assertEquals(6.2, preemPriorityCpuScheduler.getAverageWaitingTime(), 1E-6);
        assertEquals(5.4, preemPriorityCpuScheduler.getAverageResponseTime(), 1E-6);
    }

//    @Test
//    void test3() {
//        // TODO: need verified
//        Process p1 = new Process(0, 8, 3, 1);
//        Process p2 = new Process(1, 2, 4,2);
//        Process p3 = new Process(3, 4, 4, 3);
//        Process p4 = new Process(4, 1, 5, 4);
//        Process p5 = new Process(5, 6, 2, 5);
//        Process p6 = new Process(6, 5, 6, 6);
//        Process p7 = new Process(10, 1, 1, 7);
//        List<Process> processes = new ArrayList<>(List.of(p1, p2, p3, p4, p5, p6, p7));
//
//        PreemPriorityCpuScheduler preemPriorityCpuScheduler = new PreemPriorityCpuScheduler(processes, NonpreemPriorityCpuScheduler.ASCENDING);
//        preemPriorityCpuScheduler.runCpuScheduler();
//
//        assertEquals(, preemPriorityCpuScheduler.getSumTurnaroundTime());
//        assertEquals(, preemPriorityCpuScheduler.getSumWaitingTime());
//        assertEquals(, preemPriorityCpuScheduler.getSumResponseTime());
//
//        assertEquals(, preemPriorityCpuScheduler.getAverageTurnaroundTime(), 1E-6);
//        assertEquals(, preemPriorityCpuScheduler.getAverageWaitingTime(), 1E-6);
//        assertEquals(, preemPriorityCpuScheduler.getAverageResponseTime(), 1E-6);
//    }
}
