package com.krillsson.sysapi.dto.memory;

public class MemoryLoad {
    private int numberOfProcesses;

    private long swapTotal;

    private long swapUsed;

    private long total;

    private long available;
    
    public MemoryLoad(int numberOfProcesses, long swapTotal, long swapUsed, long total, long available) {
        this.numberOfProcesses = numberOfProcesses;
        this.swapTotal = swapTotal;
        this.swapUsed = swapUsed;
        this.total = total;
        this.available = available;
    }

    public MemoryLoad() {
    }

    public int getNumberOfProcesses() {
        return numberOfProcesses;
    }

    public void setNumberOfProcesses(int numberOfProcesses) {
        this.numberOfProcesses = numberOfProcesses;
    }

    public long getSwapTotal() {
        return swapTotal;
    }

    public void setSwapTotal(long swapTotal) {
        this.swapTotal = swapTotal;
    }

    public long getSwapUsed() {
        return swapUsed;
    }

    public void setSwapUsed(long swapUsed) {
        this.swapUsed = swapUsed;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getAvailable() {
        return available;
    }

    public void setAvailable(long available) {
        this.available = available;
    }
}