package se.christianjensen.maintenance.capturing.Gpu;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Gpu {
    private String name;
    private float load, clock, memoryUsed, temp, fanRpm;

    public Gpu(String name, float load, float clock, float memoryUsed, float temp, float fanRpm) {
        this.name = name;
        this.load = load;
        this.clock = clock;
        this.memoryUsed = memoryUsed;
        this.temp = temp;
        this.fanRpm = fanRpm;
    }

    public Gpu() {
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
    public float getMemoryUsed() {
        return memoryUsed;
    }

    @JsonProperty
    public float getTemp() {
        return temp;
    }

    @JsonProperty
    public float getFanRpm() {
        return fanRpm;
    }

    public void setLoad(float load) {
        this.load = load;
    }

    public void setClock(float clock) {
        this.clock = clock;
    }

    public void setMemoryUsed(float memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public void setFanRpm(float fanRpm) {
        this.fanRpm = fanRpm;
    }

    public void setName(String name) {
        this.name = name;
    }
}
