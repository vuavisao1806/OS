package algorithms;

import com.example.cpuschedulergui.algorithms.NonpreemSJFCpuScheduler;
import com.example.cpuschedulergui.algorithms.Process;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NonpreemSJFCpuSchedulerTest {
    @Test
    void simpleTest() {
        // example in class
        Process p1 = new Process(6, 1);
        Process p2 = new Process(8, 2);
        Process p3 = new Process(7, 3);
        Process p4 = new Process(3, 4);
        List<Process> processes = new ArrayList<>(List.of(p1, p2, p3, p4));

        NonpreemSJFCpuScheduler nonpreemSJFCpuScheduler = new NonpreemSJFCpuScheduler(processes);
        nonpreemSJFCpuScheduler.runCpuScheduler();

        assertEquals(52, nonpreemSJFCpuScheduler.getSumTurnaroundTime());
        assertEquals(28, nonpreemSJFCpuScheduler.getSumWaitingTime());
        assertEquals(28, nonpreemSJFCpuScheduler.getSumResponseTime());

        assertEquals(13, nonpreemSJFCpuScheduler.getAverageTurnaroundTime(), 1E-6);
        assertEquals(7, nonpreemSJFCpuScheduler.getAverageWaitingTime(), 1E-6);
        assertEquals(7, nonpreemSJFCpuScheduler.getAverageResponseTime(), 1E-6);
    }
}
