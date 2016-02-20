package com.krillsson.sysapi.sigar;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import com.krillsson.sysapi.domain.cpu.Cpu;
import com.krillsson.sysapi.domain.cpu.CpuInfo;
import com.krillsson.sysapi.domain.cpu.CpuLoad;

import java.util.ArrayList;
import java.util.List;

public class CpuSigar extends SigarWrapper {
    private static final long HACK_DELAY_MILLIS = 500;

    protected CpuSigar(Sigar sigar) {
        super(sigar);
    }

    public Cpu getCpu() {

        List<CpuLoad> cpuLoads = cpuTimesPerCore(cpuPercList());
        return new Cpu(cpuInfo(), cpuTimeSysPercent(cpuLoads),
                totalCpuTime(), cpuLoads);
    }

    public CpuLoad getCpuTimeByCoreIndex(int id) throws IllegalArgumentException {
        CpuPerc[] cpuPercs = cpuPercList();
        if (id > cpuPercs.length) {
            throw new IllegalArgumentException("No core with id " + Integer.toString(id) + " were found");
        }

        List<CpuLoad> result = cpuTimesPerCore(new CpuPerc[]{cpuPercs[id]});
        return result.get(0);
    }

    protected double cpuTimeSysPercent(List<CpuLoad> cpuLoads) {
        double userTime = 0.0;
        for (CpuLoad cpu : cpuLoads) {
            userTime += cpu.sys();
        }
        return userTime / 1.0;
    }

    protected CpuLoad totalCpuTime() {
        CpuPerc cpuPerc;
        try {
            cpuPerc = sigar.getCpuPerc();
            if (Double.isNaN(cpuPerc.getIdle())) {
                try {
                    Thread.sleep(HACK_DELAY_MILLIS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return SigarBeanConverter.fromSigarBean(cpuPerc);
                }
                cpuPerc = sigar.getCpuPerc();
            }
            return SigarBeanConverter.fromSigarBean(cpuPerc);
        } catch (SigarException e) {
            //swallow
        }
        return null;
    }

    protected List<CpuLoad> cpuTimesPerCore(CpuPerc[] percList) {
        List<CpuLoad> result = new ArrayList<>();
        for (CpuPerc cp : percList) {
            result.add(SigarBeanConverter.fromSigarBean(cp));
        }
        return result;
    }

    protected CpuInfo cpuInfo() {
        try {
            org.hyperic.sigar.CpuInfo[] infos = sigar.getCpuInfoList();
            if (infos == null || infos.length == 0) {
                return null;
            }
            return SigarBeanConverter.fromSigarBean(infos[0]);
        } catch (SigarException e) {
            // give up
            return null;
        }
    }

    protected CpuPerc[] cpuPercList() {
        CpuPerc[] cpus = null;
        try {
            cpus = sigar.getCpuPercList();
            if (Double.isNaN(cpus[0].getIdle())) {
                try {
                    Thread.sleep(HACK_DELAY_MILLIS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return cpus;
                }
                cpus = sigar.getCpuPercList();
                if (cpus == null) {
                    return cpus;
                }
            }
        } catch (SigarException e) {
            // give up
        }
        if (cpus == null || cpus.length == 0) {
            return null;
        }

        return cpus;
    }

}
