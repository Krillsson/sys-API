package se.christianjensen.maintenance.sigar;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Gauge;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class MemoryMetrics extends AbstractSigarMetric {

    protected MemoryMetrics(Sigar sigar) {
        super(sigar);
    }

    public static abstract class MemSegment {
        protected final long total;
        protected final long used;
        protected final long free;
        
        private MemSegment(long total, long used, long free) {
            this.total = total;
            this.used = used;
            this.free = free;
        }
        @JsonProperty
        public long total() { return total; }
        @JsonProperty
        public long used() { return used; }
        @JsonProperty
        public long free() { return free; }
    }

    public static final class MainMemory extends MemSegment {
        private final long actualUsed, actualFree;
        private final double usedPercent, freePercent;
    
        private MainMemory(//
                long total, long used, long free, //
                long actualUsed, long actualFree,
                double usedPercent, double freePercent) {
            super(total, used, free);
            this.actualUsed = actualUsed;
            this.actualFree = actualFree;
            this.usedPercent = usedPercent;
            this.freePercent = freePercent;
        }

        public static MainMemory fromSigarBean(Mem mem) {
            return new MainMemory( //
                    mem.getTotal(), mem.getUsed(), mem.getFree(), //
                    mem.getActualUsed(), mem.getActualFree(),
                    mem.getUsedPercent(), mem.getFreePercent());
        }

        private static MainMemory undef() {
            return new MainMemory(-1L, -1L, -1L, -1L, -1L, -1, -1);
        }
        @JsonProperty
        public long actualUsed() { return actualUsed; }
        @JsonProperty
        public long actualFree() { return actualFree; }
        @JsonProperty
        public double usedPercent() { return usedPercent; }
        @JsonProperty
        public double freePercent() { return freePercent; }
    }

    public static final class SwapSpace extends MemSegment {
        private final long pagesIn, pagesOut;

        private SwapSpace( //
                long total, long used, long free, //
                long pagesIn, long pagesOut) {
            super(total, used, free);
            this.pagesIn = pagesIn;
            this.pagesOut = pagesOut;
        }

        public static SwapSpace fromSigarBean(Swap swap) {
            return new SwapSpace( //
                    swap.getTotal(), swap.getUsed(), swap.getFree(), //
                    swap.getPageIn(), swap.getPageOut()); 
        }

        private static SwapSpace undef() {
            return new SwapSpace(-1L, -1L, -1L, -1L, -1L);
        }
        @JsonProperty

        public long pagesIn() { return pagesIn; }
        @JsonProperty

        public long pagesOut() { return pagesOut; }
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
