package algorithms;

import com.example.cpuschedulergui.algorithms.FCFSCpuScheduler;
import org.junit.jupiter.api.Test;

import com.example.cpuschedulergui.algorithms.FCFSCpuScheduler;
import com.example.cpuschedulergui.algorithms.Process;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FCFSCpuSchedulerTest {
    @Test
    void simpleTest() {
        // example in class
        Process p1 = new Process(6, 1);
        Process p2 = new Process(8, 2);
        Process p3 = new Process(7, 3);
        Process p4 = new Process(3, 4);
        List<Process> processes = new ArrayList<>(List.of(p1, p2, p3, p4));

        FCFSCpuScheduler fcfsCpuScheduler = new FCFSCpuScheduler(processes);
        fcfsCpuScheduler.runCpuScheduler();

        assertEquals(65, fcfsCpuScheduler.getSumTurnaroundTime());
        assertEquals(41, fcfsCpuScheduler.getSumWaitingTime());
        assertEquals(41, fcfsCpuScheduler.getSumResponseTime());

        assertEquals(16.25, fcfsCpuScheduler.getAverageTurnaroundTime(), 1E-6);
        assertEquals(10.25, fcfsCpuScheduler.getAverageWaitingTime(), 1E-6);
        assertEquals(10.25, fcfsCpuScheduler.getAverageResponseTime(), 1E-6);
    }
}
