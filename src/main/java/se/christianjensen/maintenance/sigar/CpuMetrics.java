package se.christianjensen.maintenance.sigar;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import se.christianjensen.maintenance.representation.cpu.Cpu;
import se.christianjensen.maintenance.representation.cpu.CpuInfo;
import se.christianjensen.maintenance.representation.cpu.CpuTime;

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
        return new Cpu(cpuInfo(), cpuTimeSysPercent(), cpuTimesPerCore(cpuPercList()));
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

    protected List<CpuTime> cpuTimesPerCore(CpuPerc[] percList) {
        List<CpuTime> result = new ArrayList<>();
        CpuPerc[] cpus = cpuPercList();
        if (cpus == null) {
            return result;
        }

        if (Double.isNaN(cpus[0].getIdle())) {
            /*
             * XXX: Hacky workaround for strange Sigar behaviour.
             * If you call sigar.getCpuPerfList() too often(?), 
             * it returns a steaming pile of NaNs. 
             *
             * See suspicious code here:
             * https://github.com/hyperic/sigar/blob/master/bindings/java/src/org/hyperic/sigar/Sigar.java#L345-348
             *
             */ 
            try {
                Thread.sleep(HACK_DELAY_MILLIS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return result;
            }
            cpus = percList;
            if (cpus == null) {
                return result;
            }
        }
        for (CpuPerc cp : cpus) {
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
        } catch (SigarException e) {
            // give up
        }
        if (cpus == null || cpus.length == 0) {
            return null;
        }
        return cpus;
    }

}
