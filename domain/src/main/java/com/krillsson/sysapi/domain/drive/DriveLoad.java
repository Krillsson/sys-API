package com.krillsson.sysapi.domain.drive;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DriveLoad
{
    private double readRateInkBs;
    private double writeRateInkBs;

    public DriveLoad(double readRateInKbs, double writeRateInkBs) {
        this.readRateInkBs = readRateInKbs;
        this.writeRateInkBs = writeRateInkBs;
    }

    @JsonProperty
    public double getReadRateInkBs() {
        return readRateInkBs;
    }

    @JsonProperty
    public double getWriteRateInkBs() {
        return writeRateInkBs;
    }
}
