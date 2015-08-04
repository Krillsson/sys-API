package se.krillsson.maintenance.sigar;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import se.krillsson.maintenance.representation.memory.MainMemory;
import se.krillsson.maintenance.representation.memory.MemoryInfo;
import se.krillsson.maintenance.representation.memory.SwapSpace;

public class MemoryMetrics extends AbstractSigarMetric {

    protected MemoryMetrics(Sigar sigar) {
        super(sigar);
    }

    public MemoryInfo getMemoryInfo() {
        return new MemoryInfo(ramInMB(), getRam(), getSwap());
    }

    public MainMemory getRam() {
        try {
            return MainMemory.fromSigarBean(sigar.getMem());
        } catch (SigarException e) {
            return MainMemory.undef();
        }
    }

    public SwapSpace getSwap() {
        try {
            return SwapSpace.fromSigarBean(sigar.getSwap());
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
