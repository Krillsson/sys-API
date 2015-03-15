package se.christianjensen.maintenance.capturing.Cpu;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Cpu {
    private String name;
    private float load, clock, temp, fanRpm;
    private List<CpuCore> cores;

    public Cpu(String name, float load, float clock, float temp, float fanRpm, List<CpuCore> cores) {
        this.name = name;
        this.load = load;
        this.clock = clock;
        this.temp = temp;
        this.fanRpm = fanRpm;
        this.cores = cores;
    }

    public Cpu() {

    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public float getLoad() {
        return load;
    }

    @JsonProperty
    public float getClock() {
        return clock;
    }

    @JsonProperty
    public float getTemp() {
        return temp;
    }

    @JsonProperty
    public List<CpuCore> getCores() {
        return cores;
    }

    @JsonProperty
    public float getFanRpm() {
        return fanRpm;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLoad(float load) {
        this.load = load;
    }

    public void setClock(float clock) {
        this.clock = clock;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public void setFanRpm(float fanRpm) {
        this.fanRpm = fanRpm;
    }

    public void setCores(List<CpuCore> cores) {
        this.cores = cores;
    }
}
