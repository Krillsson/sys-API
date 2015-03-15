package se.christianjensen.maintenance.capturing.Cpu;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CpuCore {
    public float load, clock, temp;

    public CpuCore(float load, float clock, float temp) {
        this.load = load;
        this.clock = clock;
        this.temp = temp;
    }

    public CpuCore() {

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

    public void setLoad(float load) {
        this.load = load;
    }

    public void setClock(float clock) {
        this.clock = clock;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }
}
