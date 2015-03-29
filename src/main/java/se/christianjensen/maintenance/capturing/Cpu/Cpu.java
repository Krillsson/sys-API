package se.christianjensen.maintenance.capturing.Cpu;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Cpu {
    private String name;
    private float load;
    private float averageTemp;
    private float fanRpm;
    private List<CpuCore> cores;

    public Cpu(String name, float load, float clock, float averageTemp, float fanRpm, List<CpuCore> cores) {
        this.name = name;
        this.load = load;
        this.averageTemp = averageTemp;
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
    public float getAverageTemp() {
        return averageTemp;
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

    public void setAverageTemp(float averageTemp) {
        this.averageTemp = averageTemp;
    }

    public void setFanRpm(float fanRpm) {
        this.fanRpm = fanRpm;
    }

    public void setCores(List<CpuCore> cores) {
        this.cores = cores;
    }
}
