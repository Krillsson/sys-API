package se.christianjensen.maintenance.sigar;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.junit.Before;
import org.junit.Test;
import se.christianjensen.maintenance.representation.processes.*;
import se.christianjensen.maintenance.representation.processes.Process;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ProcessMetricsTest {

    private Sigar sigar;
    private ProcessMetrics processMetrics;
    private Process process1;

    @Before
    public void setUp() throws Exception {
        sigar = mock(Sigar.class);
        processMetrics = new ProcessMetrics(sigar);
        process1 = new Process(123L, new String[0], new ProcessExecutable(), new ProcessCreator(), new ProcessCpu(), new ProcessMemory());

    }

    @Test
    public void getProcessListHappyPath() throws Exception {
        setupProcessMock();
        List<se.christianjensen.maintenance.representation.processes.Process> processes = processMetrics.getProcesses();
        assertThat(processes.get(0).getPid(), is(123L));
    }

    private void setupProcessMock() throws SigarException {
        long[] pidArray = {process1.getPid()};
        when(sigar.getProcList()).thenReturn(pidArray);
        when(sigar.getProcArgs(anyLong())).thenReturn(new String[0]);
        when(sigar.getProcExe(anyLong())).thenThrow(new SigarException());
        when(sigar.getProcCredName(anyLong())).thenThrow(new SigarException());
        when(sigar.getProcCpu(anyLong())).thenThrow(new SigarException());
        when(sigar.getProcMem(anyLong())).thenThrow(new SigarException());
    }
}