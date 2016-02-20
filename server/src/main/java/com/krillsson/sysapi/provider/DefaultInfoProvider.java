package com.krillsson.sysapi.provider;

import com.krillsson.sysapi.domain.cpu.Cpu;
import com.krillsson.sysapi.domain.cpu.CpuLoad;
import com.krillsson.sysapi.domain.drive.Drive;
import com.krillsson.sysapi.domain.drive.FileSystemType;
import com.krillsson.sysapi.domain.gpu.Gpu;
import com.krillsson.sysapi.domain.memory.MainMemory;
import com.krillsson.sysapi.domain.memory.MemoryInfo;
import com.krillsson.sysapi.domain.memory.SwapSpace;
import com.krillsson.sysapi.domain.motherboard.Motherboard;
import com.krillsson.sysapi.domain.network.NetworkInfo;
import com.krillsson.sysapi.domain.network.NetworkInterfaceConfig;
import com.krillsson.sysapi.domain.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.domain.processes.Process;
import com.krillsson.sysapi.domain.processes.ProcessStatistics;
import com.krillsson.sysapi.domain.system.JvmProperties;
import com.krillsson.sysapi.domain.system.OperatingSystem;
import com.krillsson.sysapi.domain.system.System;
import com.krillsson.sysapi.domain.system.UserInfo;
import com.krillsson.sysapi.sigar.SigarKeeper;

import java.util.List;

public class DefaultInfoProvider implements InfoProvider
{
    private SigarKeeper sigar;

    protected DefaultInfoProvider()
    {
        sigar = SigarKeeper.getInstance();
    }

    @Override
    public System systemSummary(String filesystemId, String nicId)
    {
        return sigar.system().getExtendedSystem(filesystemId, nicId);
    }

    @Override
    public System system()
    {
        return sigar.system().getSystem();
    }

    @Override
    public Motherboard motherboard()    {
        throw new RuntimeException("Sorry, not implemented");
    }

    @Override
    public Cpu cpu()
    {
        return sigar.cpu().getCpu();
    }

    @Override
    public CpuLoad getCpuTimeByCoreIndex(int id)
    {
        return sigar.cpu().getCpuTimeByCoreIndex(id);
    }

    @Override
    public List<Drive> drives()
    {
        return sigar.filesystems().getFilesystems();
    }

    @Override
    public List<Drive> getFileSystemsWithCategory(FileSystemType fsType)
    {
        return sigar.filesystems().getFileSystemsWithCategory(fsType);
    }

    @Override
    public Drive getFileSystemById(String name)
    {
        return sigar.filesystems().getFileSystemById(name);
    }

    @Override
    public MemoryInfo memoryInfo()
    {
        return sigar.memory().getMemoryInfo();
    }

    @Override
    public MainMemory ram()
    {
        return sigar.memory().getRam();
    }

    @Override
    public SwapSpace swap()
    {
        return sigar.memory().getSwap();
    }

    @Override
    public NetworkInfo networkInfo()
    {
        NetworkInfo info = sigar.network().getNetworkInfo();
        for(NetworkInterfaceConfig conf : info.getNetworkInterfaceConfigs())
        {
            conf.setNetworkInterfaceSpeed(sigar.network().getSpeed(conf.getName()));
        }
        return info;
    }

    @Override
    public NetworkInterfaceConfig getConfigById(String id)
    {
        NetworkInterfaceConfig config = sigar.network().getConfigById(id);
        config.setNetworkInterfaceSpeed(sigar.network().getSpeed(id));
        return config;
    }

    @Override
    public List<Process> processes()
    {
        return sigar.process().getProcesses();
    }

    @Override
    public ProcessStatistics statistics()
    {
        return sigar.process().getStatistics();
    }

    @Override
    public Process getProcessByPid(long pid)
    {
        return sigar.process().getProcessByPid(pid);
    }

    @Override
    public List<Gpu> gpus()
    {
       throw new RuntimeException("Sorry, not implemented");
    }

    @Override
    public List<UserInfo> users()
    {
        return sigar.system().getUsers();
    }

    @Override
    public JvmProperties jvmProperties()
    {
        return sigar.system().getJvmProperties();
    }

    @Override
    public OperatingSystem operatingSystem()
    {
        return sigar.system().getOperatingSystem();
    }

    @Override
    public double uptime()
    {
        return sigar.system().getUptime();
    }

    @Override
    public NetworkInterfaceSpeed networkSpeedById(String id)
    {
        return sigar.network().getSpeed(id);
    }
}
