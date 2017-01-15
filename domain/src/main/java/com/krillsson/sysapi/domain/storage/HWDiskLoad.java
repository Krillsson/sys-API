package com.krillsson.sysapi.domain.storage;

public class HWDiskLoad {
    private double readRateInkBs;
    private double writeRateInkBs;

    public HWDiskLoad(double readRateInKbs, double writeRateInkBs) {
        this.readRateInkBs = readRateInKbs;
        this.writeRateInkBs = writeRateInkBs;
    }

    public double getReadRateInkBs() {
        return readRateInkBs;
    }

    public double getWriteRateInkBs() {
        return writeRateInkBs;
    }
}
