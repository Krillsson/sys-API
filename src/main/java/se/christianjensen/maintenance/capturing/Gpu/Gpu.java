package se.christianjensen.maintenance.capturing.Gpu;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Gpu {
    private double load, clock, memoryUsed, temp, fanRpm;

    public Gpu(double load, double clock, double memoryUsed, double temp, double fanRpm) {
        this.load = load;
        this.clock = clock;
        this.memoryUsed = memoryUsed;
        this.temp = temp;
        this.fanRpm = fanRpm;
    }

    @JsonProperty
    public double getLoad() {
        return load;
    }

    @JsonProperty
    public double getClock() {
        return clock;
    }

    @JsonProperty
    public double getMemoryUsed() {
        return memoryUsed;
    }

    @JsonProperty
    public double getTemp() {
        return temp;
    }

    @JsonProperty
    public double getFanRpm() {
        return fanRpm;
    }
}
