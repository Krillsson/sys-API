package com.krillsson.sysapi.dto.sensors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.krillsson.sysapi.dto.cpu.CpuHealth;
import com.krillsson.sysapi.dto.gpu.GpuHealth;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "cpuHealth",
        "gpusHealth",
        "healthData"
})
public class SensorsInfo {

    @JsonProperty("cpuHealth")
    private CpuHealth cpuHealth;
    @JsonProperty("gpusHealth")
    private Map<String, GpuHealth> gpusHealth;
    @JsonProperty("healthData")
    private HealthData[] healthData;

    /**
     * No args constructor for use in serialization
     */
    public SensorsInfo() {
    }

    /**
     * @param healthData
     * @param cpuHealth
     * @param gpusHealth
     */
    public SensorsInfo(CpuHealth cpuHealth, Map<String, GpuHealth> gpusHealth, HealthData[] healthData) {
        super();
        this.cpuHealth = cpuHealth;
        this.gpusHealth = gpusHealth;
        this.healthData = healthData;
    }

    @JsonProperty("cpuHealth")
    public CpuHealth getCpuHealth() {
        return cpuHealth;
    }

    @JsonProperty("cpuHealth")
    public void setCpuHealth(CpuHealth cpuHealth) {
        this.cpuHealth = cpuHealth;
    }

    @JsonProperty("gpuHealths")
    public Map<String, GpuHealth> getGpuHealths() {
        return gpusHealth;
    }

    @JsonProperty("gpuHealths")
    public void setGpuHealths(Map<String, GpuHealth> gpuHealths) {
        this.gpusHealth = gpuHealths;
    }

    @JsonProperty("healthDatas")
    public HealthData[] getHealthData() {
        return healthData;
    }

    @JsonProperty("healthDatas")
    public void setHealthData(HealthData[] healthDatas) {
        this.healthData = healthDatas;
    }

}
