package se.christianjensen.maintenance.capturing.Cpu;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Cpu {
    private double load, clock, temp, fanRpm;
    private List<CpuCore> cores;

    public Cpu(double load, double clock, double temp, double fanRpm, List<CpuCore> cores) {
        this.load = load;
        this.clock = clock;
        this.temp = temp;
        this.fanRpm = fanRpm;
        this.cores = cores;
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

    @JsonProperty
    public List<CpuCore> getCores() {
        return cores;
    }

    @JsonProperty
    public double getFanRpm() {
        return fanRpm;
    }
}
