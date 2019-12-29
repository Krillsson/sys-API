package com.krillsson.sysapi.core.domain.memory;

public class MemoryLoad {
    private int numberOfProcesses;
    private long total;
    private long swapTotal;
    private long available;
    private long swapUsed;

    public MemoryLoad(int numberOfProcesses, long swapTotal, long swapUsed, long total, long available) {
        this.numberOfProcesses = numberOfProcesses;
        this.swapTotal = swapTotal;
        this.swapUsed = swapUsed;
        this.total = total;
        this.available = available;
    }

    public int getNumberOfProcesses() {
        return numberOfProcesses;
    }

    public long getSwapTotal() {
        return swapTotal;
    }

    public long getSwapUsed() {
        return swapUsed;
    }

    public long getTotal() {
        return total;
    }

    public long getAvailable() {
        return available;
    }
}
