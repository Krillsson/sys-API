package com.krillsson.sysapi.sigar;

import com.krillsson.sysapi.domain.cpu.CpuInfo;
import com.krillsson.sysapi.domain.cpu.CpuLoad;
import com.krillsson.sysapi.domain.filesystem.Drive;
import com.krillsson.sysapi.domain.filesystem.FileSystemType;
import com.krillsson.sysapi.domain.filesystem.FileSystemUsage;
import com.krillsson.sysapi.domain.memory.MainMemory;
import com.krillsson.sysapi.domain.memory.SwapSpace;
import com.krillsson.sysapi.domain.network.NetworkInfo;
import com.krillsson.sysapi.domain.network.NetworkInterfaceConfig;
import com.krillsson.sysapi.domain.network.NetworkInterfaceStatistics;
import com.krillsson.sysapi.domain.processes.*;
import com.krillsson.sysapi.domain.system.*;
import org.hyperic.sigar.*;
import org.hyperic.sigar.OperatingSystem;

import java.util.List;

public class SigarBeanConverter {
    public static CpuLoad fromSigarBean(CpuPerc cp) {
        return new CpuLoad(
                temperature, cp.getUser(), cp.getSys(),
                cp.getNice(), cp.getWait(),
                cp.getIdle(), cp.getIrq());
    }

    public static Drive fromSigarBean(org.hyperic.sigar.FileSystem fs, FileSystemUsage usage) {
        return new Drive(fs.getDevName(), fs.getDirName(), //
                FileSystemType.values()[fs.getType()], fs.getSysTypeName(), usage);
    }

    public static CpuInfo fromSigarBean(org.hyperic.sigar.CpuInfo sigarCpuInfo) {
        return new CpuInfo(sigarCpuInfo.getVendor()
                , sigarCpuInfo.getModel().trim()
                , sigarCpuInfo.getMhz()
                , sigarCpuInfo.getCacheSize()
                , sigarCpuInfo.getTotalCores()
                , sigarCpuInfo.getTotalSockets()
                , sigarCpuInfo.getCoresPerSocket());
    }

    public static FileSystemUsage fromSigarBean(org.hyperic.sigar.FileSystemUsage usage)
    {
        return new FileSystemUsage(usage.getTotal(),
                usage.getFree(),
                usage.getUsed(),
                usage.getAvail(),
                usage.getFiles(),
                usage.getFreeFiles(),
                usage.getDiskReads(),
                usage.getDiskWrites(),
                usage.getDiskReadBytes(),
                usage.getDiskWriteBytes(),
                usage.getDiskQueue(),
                usage.getDiskServiceTime(),
                usage.getUsePercent());
    }

    public static MainMemory fromSigarBean(Mem mem) {
        return new MainMemory( //
                mem.getTotal(), mem.getUsed(), mem.getFree(), //
                mem.getActualUsed(), mem.getActualFree(),
                mem.getUsedPercent(), mem.getFreePercent());
    }

    public static SwapSpace fromSigarBean(Swap swap) {
        return new SwapSpace(
                swap.getTotal(), swap.getUsed(), swap.getFree(),
                swap.getPageIn(), swap.getPageOut());
    }

    public static NetworkInfo fromSigarBean(NetInfo ni, List<NetworkInterfaceConfig> configs) {
        return new NetworkInfo(ni.getDefaultGateway()
                , ni.getHostName(), ni.getDomainName()
                , ni.getPrimaryDns(), ni.getSecondaryDns()
                ,configs);
    }

    public static NetworkInterfaceConfig fromSigarBean(NetInterfaceConfig nIC) {
        return new NetworkInterfaceConfig(nIC.getName(), nIC.getHwaddr(),
                nIC.getType(), nIC.getDescription(),
                nIC.getAddress(), nIC.getDestination(),
                nIC.getBroadcast(), nIC.getNetmask(),
                nIC.getFlags(), nIC.getMtu(), nIC.getMetric());
    }

    public static NetworkInterfaceStatistics fromSigarBean(NetInterfaceStat nIS) {
        return new NetworkInterfaceStatistics(nIS.getRxBytes(), nIS.getRxPackets(),
                nIS.getRxErrors(), nIS.getRxDropped(),
                nIS.getRxOverruns(), nIS.getRxFrame(),
                nIS.getTxBytes(), nIS.getTxPackets(),
                nIS.getTxErrors(), nIS.getTxDropped(),
                nIS.getTxOverruns(), nIS.getTxCollisions(),
                nIS.getTxCarrier(), nIS.getSpeed());
    }

    public static ProcessCpu fromSigarBean(ProcCpu procCpu){
        return new ProcessCpu(procCpu.getPercent(), procCpu.getLastTime(), procCpu.getStartTime(), procCpu.getUser(), procCpu.getSys(), procCpu.getTotal());
    }

    public static ProcessCreator fromSigarBean(ProcCredName procCredName){
        return new ProcessCreator(procCredName.getUser(), procCredName.getGroup());
    }

    public static ProcessExecutable fromSigarBean(ProcExe procExe) {
        return new ProcessExecutable(procExe.getName(), procExe.getCwd());
    }

    public static ProcessMemory fromSigarBean(ProcMem procMem) {
        return new ProcessMemory(procMem.getSize(), procMem.getResident(), procMem.getShare(), procMem.getMinorFaults(), procMem.getMajorFaults(), procMem.getPageFaults());
    }

    public static ProcessState fromSigarBean(ProcState procState)
    {
        ProcessState.State state = ProcessState.State.UNKOWN;
        switch (procState.getState())
        {
            case 'S': state = ProcessState.State.SLEEP;
                break;
            case 'R': state = ProcessState.State.RUN;
                break;
            case 'T': state = ProcessState.State.STOP;
                break;
            case 'Z': state = ProcessState.State.ZOMBIE;
                break;
            case 'D': state = ProcessState.State.IDLE;
                break;
        }
        return new ProcessState(state, procState.getName(), procState.getPpid(), procState.getTty(), procState.getNice(), procState.getPriority(), procState.getThreads(), procState.getProcessor());
    }

    public static ProcessStatistics fromSigarBean(ProcStat procStat) {
        return new ProcessStatistics(procStat.getTotal(),
                procStat.getIdle(),
                procStat.getRunning(),
                procStat.getSleeping(),
                procStat.getStopped(),
                procStat.getZombie(),
                procStat.getThreads());
    }

    public static com.krillsson.sysapi.domain.system.OperatingSystem fromSigarBean(OperatingSystem os) {
        return new com.krillsson.sysapi.domain.system.OperatingSystem(os.getName(),
                os.getVersion(),
                os.getArch(),
                os.getMachine(),
                os.getDescription(),
                os.getPatchLevel(),
                os.getVendor(),
                os.getVendorVersion(),
                os.getVendorName(),
                os.getVendorCodeName(),
                os.getCpuEndian(),
                os.getDataModel()
        );
    }

    public static UserInfo fromSigarBean(Who who) {
        return new UserInfo(who.getUser(), who.getDevice(), who.getHost(), who.getTime());
    }
}
