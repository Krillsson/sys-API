package com.krillsson.sysapi.dto.memory;

public class GlobalMemory {


    private long swapTotal;

    private long swapUsed;

    private long total;

    private long available;

    /**
     * No args constructor for use in serialization
     */
    public GlobalMemory() {
    }

    /**
     * @param total
     * @param swapUsed
     * @param swapTotal
     * @param available
     */
    public GlobalMemory(long swapTotal, long swapUsed, long total, long available) {
        super();
        this.swapTotal = swapTotal;
        this.swapUsed = swapUsed;
        this.total = total;
        this.available = available;
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
