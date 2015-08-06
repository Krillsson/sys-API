package com.krillsson.sysapi.sigar;

import org.hyperic.sigar.Sigar;

public class SigarKeeper {
    private static final SigarKeeper instance = new SigarKeeper();

    public static SigarKeeper getInstance() {
        return instance;
    }

    private final Sigar sigar = new Sigar();
    private final CpuSigar cpu = new CpuSigar(sigar);
    private final MemorySigar memory = new MemorySigar(sigar);
    private final FilesystemSigar fs = new FilesystemSigar(sigar);
    private final SystemSigar system = new SystemSigar(sigar, this);
    private final NetworkSigar network = new NetworkSigar(sigar);
    private final ProcessSigar process = new ProcessSigar(sigar);

    private SigarKeeper() {
        // singleton
    }


    public long pid() {
        return sigar.getPid();
    }

    public CpuSigar cpu() {
        return cpu;
    }

    public MemorySigar memory() {
        return memory;
    }

    public FilesystemSigar filesystems() {
        return fs;
    }

    public SystemSigar system() {
        return system;
    }

    public NetworkSigar network(){
        return network;
    }

    public ProcessSigar process() {
        return process;
    }
}
