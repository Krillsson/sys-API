package se.christianjensen.maintenance.sigar;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import org.hyperic.sigar.Sigar;

public class SigarMetrics{
    private static final SigarMetrics instance = new SigarMetrics();

    public static SigarMetrics getInstance() {
        return instance;
    }

    private final Sigar sigar = new Sigar();
    private final CpuMetrics cpu = new CpuMetrics(sigar); 
    private final MemoryMetrics memory = new MemoryMetrics(sigar); 
    private final FilesystemMetrics fs = new FilesystemMetrics(sigar); 

    private SigarMetrics() {
        // singleton
    }


    public long pid() {
        return sigar.getPid();
    }

    public CpuMetrics cpu() {
        return cpu;
    }

    public MemoryMetrics memory() {
        return memory;
    }

    public FilesystemMetrics filesystems() {
        return fs;
    }


}
