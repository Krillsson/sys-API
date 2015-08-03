package se.christianjensen.maintenance.sigar;

import org.hyperic.sigar.Sigar;

public class SigarMetrics {
    private static final SigarMetrics instance = new SigarMetrics();

    public static SigarMetrics getInstance() {
        return instance;
    }

    private final Sigar sigar = new Sigar();
    private final CpuMetrics cpu = new CpuMetrics(sigar);
    private final MemoryMetrics memory = new MemoryMetrics(sigar);
    private final FilesystemMetrics fs = new FilesystemMetrics(sigar);
    private final SystemMetrics system = new SystemMetrics(sigar);
    private final NetworkMetrics network = new NetworkMetrics(sigar);
    private final ProcessMetrics process = new ProcessMetrics(sigar);

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

    public SystemMetrics system() {
        return system;
    }

    public NetworkMetrics network(){
        return network;
    }

    public ProcessMetrics process() {
        return process;
    }
}
