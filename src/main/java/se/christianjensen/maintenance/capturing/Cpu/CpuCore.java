package se.christianjensen.maintenance.capturing.Cpu;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CpuCore {
    private double load, clock, temp;

    public CpuCore(double load, double clock, double temp) {
        this.load = load;
        this.clock = clock;
        this.temp = temp;
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
    public double getTemp() {
        return temp;
    }
}
