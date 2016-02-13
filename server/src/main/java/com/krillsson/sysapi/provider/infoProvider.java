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
import com.krillsson.sysapi.domain.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.domain.processes.Process;
import com.krillsson.sysapi.domain.processes.ProcessStatistics;
import com.krillsson.sysapi.domain.system.JvmProperties;
import com.krillsson.sysapi.domain.system.OperatingSystem;
import com.krillsson.sysapi.domain.system.System;
import com.krillsson.sysapi.domain.system.UserInfo;

import java.util.List;

public interface InfoProvider
{
    System systemSummary(String filesystemId, String nicId);
    System system();
    Motherboard motherboard();

    Cpu cpu();
    CpuLoad getCpuTimeByCoreIndex(int id);

    List<Drive> drives();
    List<Drive> getFileSystemsWithCategory(FileSystemType fsType);
    Drive getFileSystemById(String name);

    MemoryInfo memoryInfo();
    MainMemory ram();
    SwapSpace swap();

    NetworkInfo networkInfo();
    NetworkInterfaceConfig getConfigById(String id);

    List<Process> processes();
    ProcessStatistics statistics();
    Process getProcessByPid(long pid);

    List<Gpu> gpus();

    List<UserInfo> users();

    JvmProperties jvmProperties();

    OperatingSystem operatingSystem();

    double uptime();

    NetworkInterfaceSpeed networkSpeedById(String id);
}
