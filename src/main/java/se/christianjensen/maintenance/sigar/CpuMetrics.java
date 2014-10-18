package se.christianjensen.maintenance.sigar;

import java.util.List;
import java.util.ArrayList;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.RatioGauge;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class CpuMetrics extends AbstractSigarMetric {
    private static final long HACK_DELAY_MILLIS = 1000;

    private final CpuInfo info;

    public static final class CpuTime {

        private final double user;
        private final double sys;
        private final double nice;
        private final double waiting;
        private final double idle;
        private final double irq;
    
        public CpuTime( //
                double user, double sys, //
                double nice, double waiting, //
                double idle, double irq) {
            this.user = user;
            this.sys = sys;
            this.nice = nice;
            this.waiting = waiting;
            this.idle = idle;
            this.irq = irq;
        }

        public static CpuTime fromSigarBean(CpuPerc cp) {
            return new CpuTime( //
                    cp.getUser(), cp.getSys(), //
                    cp.getNice(), cp.getWait(), //
                    cp.getIdle(), cp.getIrq());
        }
        @JsonProperty
        public double user() { return user; }
        @JsonProperty
        public double sys() { return sys; }
        @JsonProperty
        public double nice() { return nice; }
        @JsonProperty
        public double waiting() { return waiting; }
        @JsonProperty
        public double idle() { return idle; }
        @JsonProperty
        public double irq() { return irq; }
    }



    public double cpuTimeSysPercent(){
        List<CpuTime> cpus = cpus();
        double userTime = 0.0;
        for (CpuTime cpu : cpus) {
            userTime += cpu.sys();
        }
        return userTime / 1.0;
    }

    protected CpuMetrics(Sigar sigar) {
        super(sigar);
        info = cpuInfo();
    }
    @JsonProperty
    public int totalCoreCount() {
        if (info == null) {
            return -1;
        }
        return info.getTotalCores(); 
    }
    @JsonProperty
    public int physicalCpuCount() {
        if (info == null) {
            return -1;
        }
       return info.getTotalSockets();
    }
    @JsonProperty
    public List<CpuTime> cpus() {
        List<CpuTime> result = new ArrayList<CpuTime>();
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
            cpus = cpuPercList();
            if (cpus == null) {
                return result;
            }
        }
        for (CpuPerc cp : cpus) {
            result.add(CpuTime.fromSigarBean(cp)); 
        }
        return result;
   }
    @JsonProperty
    private CpuInfo cpuInfo() {
        try {
            CpuInfo[] infos = sigar.getCpuInfoList();
            if (infos == null || infos.length == 0) {
                return null;
            }
            return infos[0];
        } catch (SigarException e) {
            // give up
            return null;
        }
    }
    @JsonProperty
    private CpuPerc[] cpuPercList() {
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
