package com.krillsson.sysapi.provider;

import com.krillsson.sysapi.domain.cpu.Cpu;
import com.krillsson.sysapi.domain.cpu.CpuLoad;
import com.krillsson.sysapi.domain.filesystem.Drive;
import com.krillsson.sysapi.domain.filesystem.FileSystemType;
import com.krillsson.sysapi.domain.gpu.Gpu;
import com.krillsson.sysapi.domain.memory.MainMemory;
import com.krillsson.sysapi.domain.memory.MemoryInfo;
import com.krillsson.sysapi.domain.memory.SwapSpace;
import com.krillsson.sysapi.domain.motherboard.Motherboard;
import com.krillsson.sysapi.domain.network.NetworkInfo;
import com.krillsson.sysapi.domain.network.NetworkInterfaceConfig;
import com.krillsson.sysapi.domain.processes.Process;
import com.krillsson.sysapi.domain.processes.ProcessStatistics;
import com.krillsson.sysapi.domain.system.System;
import com.krillsson.sysapi.domain.system.UserInfo;

import java.util.List;

public class DefaultInfoProvider implements InfoProvider
{
    @Override
    public System systemSummary(String filesystemId, String nicId)
    {
        return null;
    }

    @Override
    public Motherboard motherboard()
    {
        return null;
    }

    @Override
    public List<Cpu> cpu()
    {
        return null;
    }

    @Override
    public CpuLoad getCpuTimeByCoreIndex(int id)
    {
        return null;
    }

    @Override
    public List<Drive> filesystems()
    {
        return null;
    }

    @Override
    public List<Drive> getFileSystemsWithCategory(FileSystemType fsType)
    {
        return null;
    }

    @Override
    public Drive getFileSystemById(String name)
    {
        return null;
    }

    @Override
    public MemoryInfo memoryInfo()
    {
        return null;
    }

    @Override
    public MainMemory ram()
    {
        return null;
    }

    @Override
    public SwapSpace swap()
    {
        return null;
    }

    @Override
    public NetworkInfo networkInfo()
    {
        return null;
    }

    @Override
    public NetworkInterfaceConfig getConfigById(String id)
    {
        return null;
    }

    @Override
    public List<Process> processes()
    {
        return null;
    }

    @Override
    public ProcessStatistics statistics()
    {
        return null;
    }

    @Override
    public Process getProcessByPid(long pid)
    {
        return null;
    }

    @Override
    public List<Gpu> gpu()
    {
        return null;
    }

    @Override
    public List<UserInfo> getUsers()
    {
        return null;
    }
}
