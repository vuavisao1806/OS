package algorithms;

import com.example.cpuschedulergui.algorithms.NonpreemPriorityCpuScheduler;
import com.example.cpuschedulergui.algorithms.Process;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NonpreemPriorityCpuSchedulerTest {
    @Test
    void simpleTest() {
        // page 212 chapter 5 OS
        Process p1 = new Process(0, 10, 3,1);
        Process p2 = new Process(0, 1, 1,2);
        Process p3 = new Process(0, 2, 4, 3);
        Process p4 = new Process(0, 1, 5, 4);
        Process p5 = new Process(0, 5, 2, 5);
        List<Process> processes = new ArrayList<>(List.of(p1, p2, p3, p4, p5));

        NonpreemPriorityCpuScheduler nonpreemPriorityCpuScheduler = new NonpreemPriorityCpuScheduler(processes, NonpreemPriorityCpuScheduler.ASCENDING);
        nonpreemPriorityCpuScheduler.runCpuScheduler();

        assertEquals(60, nonpreemPriorityCpuScheduler.getSumTurnaroundTime());
        assertEquals(41, nonpreemPriorityCpuScheduler.getSumWaitingTime());
        assertEquals(41, nonpreemPriorityCpuScheduler.getSumResponseTime());

        assertEquals(12, nonpreemPriorityCpuScheduler.getAverageTurnaroundTime(), 1E-6);
        assertEquals(8.2, nonpreemPriorityCpuScheduler.getAverageWaitingTime(), 1E-6);
        assertEquals(8.2, nonpreemPriorityCpuScheduler.getAverageResponseTime(), 1E-6);
    }
}
