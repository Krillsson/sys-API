package com.krillsson.sysapi.domain.memory;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hyperic.sigar.Swap;

public final class SwapSpace extends MemSegment {
    private final long pagesIn, pagesOut;

    public SwapSpace( //
                       long total, long used, long free, //
                       long pagesIn, long pagesOut) {
        super(total, used, free);
        this.pagesIn = pagesIn;
        this.pagesOut = pagesOut;
    }

    public static SwapSpace undef() {
        return new SwapSpace(-1L, -1L, -1L, -1L, -1L);
    }

    @JsonProperty
    public long pagesIn() { return pagesIn; }

    @JsonProperty
    public long pagesOut() { return pagesOut; }
}
