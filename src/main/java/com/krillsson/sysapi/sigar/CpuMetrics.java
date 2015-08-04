package com.krillsson.sysapi.sigar;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import com.krillsson.sysapi.representation.cpu.Cpu;
import com.krillsson.sysapi.representation.cpu.CpuInfo;
import com.krillsson.sysapi.representation.cpu.CpuTime;

import java.util.ArrayList;
import java.util.List;

public class CpuMetrics extends AbstractSigarMetric {
    private static final long HACK_DELAY_MILLIS = 500;

    private final CpuInfo info;

    protected CpuMetrics(Sigar sigar) {
        super(sigar);
        info = cpuInfo();
    }

    public Cpu getCpu() {
        return new Cpu(cpuInfo(), cpuTimeSysPercent(), totalCpuTime(), cpuTimesPerCore(cpuPercList()));
    }

    public CpuTime getCpuTimeByCoreIndex(int id) throws IllegalArgumentException {
        CpuPerc[] cpuPercs = cpuPercList();
        if (id > cpuPercs.length) {
            throw new IllegalArgumentException("No core with id " + Integer.toString(id) + " were found");
        }

        List<CpuTime> result = cpuTimesPerCore(new CpuPerc[]{cpuPercs[id]});
        return result.get(0);
    }

    protected double cpuTimeSysPercent() {
        List<CpuTime> cpus = cpuTimesPerCore(cpuPercList());
        double userTime = 0.0;
        for (CpuTime cpu : cpus) {
            userTime += cpu.sys();
        }
        return userTime / 1.0;
    }

    protected CpuTime totalCpuTime() {
        CpuPerc cpuPerc;
        try {
            cpuPerc = sigar.getCpuPerc();
            if (Double.isNaN(cpuPerc.getIdle())) {
                try {
                    Thread.sleep(HACK_DELAY_MILLIS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return CpuTime.fromSigarBean(cpuPerc);
                }
                cpuPerc = sigar.getCpuPerc();
            }
            return CpuTime.fromSigarBean(cpuPerc);
        } catch (SigarException e) {
            //swallow
        }
        return null;
    }

    protected List<CpuTime> cpuTimesPerCore(CpuPerc[] percList) {
        List<CpuTime> result = new ArrayList<>();
        for (CpuPerc cp : percList) {
            result.add(CpuTime.fromSigarBean(cp));
        }
        return result;
    }

    protected CpuInfo cpuInfo() {
        try {
            org.hyperic.sigar.CpuInfo[] infos = sigar.getCpuInfoList();
            if (infos == null || infos.length == 0) {
                return null;
            }
            return CpuInfo.fromSigarBean(infos[0]);
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
