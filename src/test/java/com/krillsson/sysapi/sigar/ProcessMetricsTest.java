package com.krillsson.sysapi.sigar;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.junit.Before;
import org.junit.Test;
import com.krillsson.sysapi.representation.processes.*;
import com.krillsson.sysapi.representation.processes.Process;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ProcessMetricsTest {

    private static final long process1Pid = 123L;
    private Sigar sigar;
    private ProcessMetrics processMetrics;
    private Process process1;

    @Before
    public void setUp() throws Exception {
        sigar = mock(Sigar.class);
        processMetrics = new ProcessMetrics(sigar);
        process1 = new Process(process1Pid, new String[0], new ProcessExecutable(), new ProcessCreator(), new ProcessState(), new ProcessCpu(), new ProcessMemory());

    }

    @Test
    public void getProcessListHappyPath() throws Exception {
        setupProcessMock();
        List<com.krillsson.sysapi.representation.processes.Process> processes = processMetrics.getProcesses();
        assertThat(processes.get(0).getPid(), is(process1Pid));
    }

    @Test
    public void getProcessListNoProcesses() throws Exception {
        when(sigar.getProcList()).thenReturn(new long[0]);
        List<Process> processes = processMetrics.getProcesses();
        assertTrue(processes.isEmpty());
    }

    @Test
    public void getProcessByPidHappyPath() throws Exception {
        setupProcessMock();
        Process processByPid = processMetrics.getProcessByPid(process1Pid);
        assertEquals(processByPid.getPid(), process1.getPid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getProcessByPidNonExistingProcessShouldThrowException() throws Exception {
        when(sigar.getProcList()).thenReturn(new long[0]);
        processMetrics.getProcessByPid(124L);
    }

    private void setupProcessMock() throws SigarException {
        long[] pidArray = {process1.getPid()};
        when(sigar.getProcList()).thenReturn(pidArray);
        when(sigar.getProcArgs(anyLong())).thenReturn(new String[0]);
        when(sigar.getProcExe(anyLong())).thenThrow(new SigarException());
        when(sigar.getProcCredName(anyLong())).thenThrow(new SigarException());
        when(sigar.getProcCpu(anyLong())).thenThrow(new SigarException());
        when(sigar.getProcMem(anyLong())).thenThrow(new SigarException());
        when(sigar.getProcState(anyLong())).thenThrow(new SigarException());
    }
}