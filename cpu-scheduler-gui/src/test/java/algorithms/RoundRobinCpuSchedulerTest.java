package algorithms;

import com.example.cpuschedulergui.algorithms.RoundRobinCpuScheduler;
import com.example.cpuschedulergui.algorithms.Process;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoundRobinCpuSchedulerTest {
    @Test
    void simpleTest() {
        // example in class
        // TODO: need verified
        Process p1 = new Process( 24, 1);
        Process p2 = new Process( 3,2);
        Process p3 = new Process( 3, 3);
        List<Process> processes = new ArrayList<>(List.of(p1, p2, p3));
        RoundRobinCpuScheduler roundRobinCpuScheduler = new RoundRobinCpuScheduler(processes, 4);
        roundRobinCpuScheduler.runCpuScheduler();


//        assertEquals(65, roundRobinCpuScheduler.getSumTurnaroundTime());
        assertEquals(17, roundRobinCpuScheduler.getSumWaitingTime());
//        assertEquals(41, roundRobinCpuScheduler.getSumResponseTime());

//        assertEquals(16.25, roundRobinCpuScheduler.getAverageTurnaroundTime(), 1E-6);
        assertEquals((double) 17 / 3, roundRobinCpuScheduler.getAverageWaitingTime(), 1E-6);
//        assertEquals(10.25, roundRobinCpuScheduler.getAverageResponseTime(), 1E-6);
    }
}
