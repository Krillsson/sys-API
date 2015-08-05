package com.krillsson.sysapi.domain.memory;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemoryInfo {
    private long ramInMB;
    private MainMemory mainMemory;
    private SwapSpace swapSpace;

    public MemoryInfo(long ramInMB, MainMemory mainMemory, SwapSpace swapSpace) {
        this.ramInMB = ramInMB;
        this.mainMemory = mainMemory;
        this.swapSpace = swapSpace;
    }

    @JsonProperty
    public long getRamInMB() {
        return ramInMB;
    }

    @JsonProperty
    public MainMemory getMainMemory() {
        return mainMemory;
    }

    @JsonProperty
    public SwapSpace getSwapSpace() {
        return swapSpace;
    }
}
