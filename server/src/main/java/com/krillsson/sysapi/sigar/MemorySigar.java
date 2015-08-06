package com.krillsson.sysapi.sigar;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import com.krillsson.sysapi.domain.memory.MainMemory;
import com.krillsson.sysapi.domain.memory.MemoryInfo;
import com.krillsson.sysapi.domain.memory.SwapSpace;

public class MemorySigar extends SigarWrapper {

    protected MemorySigar(Sigar sigar) {
        super(sigar);
    }

    public MemoryInfo getMemoryInfo() {
        return new MemoryInfo(ramInMB(), getRam(), getSwap());
    }

    public MainMemory getRam() {
        try {
            return SigarBeanConverter.fromSigarBean(sigar.getMem());
        } catch (SigarException e) {
            return MainMemory.undef();
        }
    }

    public SwapSpace getSwap() {
        try {
            return SigarBeanConverter.fromSigarBean(sigar.getSwap());
        } catch (SigarException e) {
            return SwapSpace.undef();
        }
    }

    public long ramInMB() {
        try {
            return sigar.getMem().getRam();
        } catch (SigarException e) {
            return -1L;
        }
    }

}
