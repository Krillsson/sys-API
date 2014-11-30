package se.christianjensen.maintenance.sigar;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import se.christianjensen.maintenance.representation.memory.MainMemory;
import se.christianjensen.maintenance.representation.memory.SwapSpace;

public class MemoryMetrics extends AbstractSigarMetric {

    protected MemoryMetrics(Sigar sigar) {
        super(sigar);
    }

    @JsonProperty
    public MainMemory mem() {
        try {
            return MainMemory.fromSigarBean(sigar.getMem());
        } catch (SigarException e) {
            return MainMemory.undef();
        }
    }
    @JsonProperty
    public SwapSpace swap() {
        try {
            return SwapSpace.fromSigarBean(sigar.getSwap());
        } catch (SigarException e) {
            return SwapSpace.undef();
        }
    }
    @JsonProperty
    public long ramInMB() {
        try {
            return sigar.getMem().getRam();
        } catch (SigarException e) {
            return -1L;
        }
    }

}
