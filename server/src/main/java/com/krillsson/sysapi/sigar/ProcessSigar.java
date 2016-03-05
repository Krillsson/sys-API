package com.krillsson.sysapi.sigar;

import com.krillsson.sysapi.domain.processes.*;
import com.krillsson.sysapi.domain.processes.Process;
import org.hyperic.sigar.ProcUtil;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ProcessSigar extends SigarWrapper {

    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ProcessSigar.class.getSimpleName());

    protected ProcessSigar(Sigar sigar) {
        super(sigar);
    }

    public List<Process> getProcesses()
    {
        List<Process> result = new ArrayList<>();
        try {
            long[] procList = sigar.getProcList();
            for(long pid : procList)
            {
                result.add(getProcess(pid));
            }
        } catch (SigarException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Process getProcessByPid(long pid) throws IllegalArgumentException {
        if(pidExists(pid))
        {
            return getProcess(pid);
        }
        else throw new IllegalArgumentException("No process with pid: " + Long.toString(pid) + " were found");
    }

    public ProcessStatistics getStatistics() {
        try {
            return SigarBeanConverter.fromSigarBean(sigar.getProcStat());
        } catch (SigarException e) {
            LOGGER.warn("Getting statistics failed", e);
            return new ProcessStatistics();
        }
    }

    private Process getProcess(long pid) {
        String[] args = getProcArgs(pid);
        ProcessExecutable executable = getExecutable(pid);
        ProcessCreator creator = getProcessCreator(pid);
        ProcessCpu cpu = getProcessCpu(pid);
        ProcessMemory memory = getMemory(pid);
        ProcessState state = getState(pid);
        return new Process(pid, getDescription(pid), args, executable, creator, state, cpu, memory);
    }

    private ProcessState getState(long pid) {
        try {
            return SigarBeanConverter.fromSigarBean(sigar.getProcState(pid));
        } catch (SigarException e) {
            return new ProcessState();
        }
    }

    private String getDescription(long pid)
    {
        try {
            return ProcUtil.getDescription(sigar, pid);
        } catch (SigarException e) {
            return "N/A";
        }
    }

    private boolean pidExists(long pid) {
        try {
            boolean exists = false;
            long[] list = sigar.getProcList();
            for(long i: list)
            {
                if(i == pid)
                {
                    exists = true;
                    break;
                }
            }
            return exists;
        } catch (SigarException e) {
            return false;
        }
    }

    private ProcessMemory getMemory(long pid){
        try {
            return SigarBeanConverter.fromSigarBean(sigar.getProcMem(pid));
        } catch (SigarException e) {
            return new ProcessMemory();
        }
    }

    private ProcessCpu getProcessCpu(long pid) {
        try {
            return SigarBeanConverter.fromSigarBean(sigar.getProcCpu(pid));
        } catch (SigarException e) {
            return new ProcessCpu();
        }
    }

    private ProcessCreator getProcessCreator(long pid) {
        try {
            return SigarBeanConverter.fromSigarBean(sigar.getProcCredName(pid));
        } catch (SigarException e) {
            return new ProcessCreator();
        }
    }

    private String[] getProcArgs(long pid){
        try {
            return sigar.getProcArgs(pid);
        } catch (SigarException e) {
            return new String[0];
        }
    }

    private ProcessExecutable getExecutable(long pid) {
        try {
            return SigarBeanConverter.fromSigarBean(sigar.getProcExe(pid));
        } catch (SigarException e) {
            return new ProcessExecutable();
        }
    }
}
