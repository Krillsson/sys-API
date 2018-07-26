package com.krillsson.sysapi.dto.sensors;

import com.krillsson.sysapi.dto.cpu.CpuHealth;
import com.krillsson.sysapi.dto.gpu.GpuHealth;

import java.util.Map;


public class SensorsInfo {


    private CpuHealth cpuHealth;

    private Map<String, GpuHealth> gpusHealth;

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


    public CpuHealth getCpuHealth() {
        return cpuHealth;
    }


    public void setCpuHealth(CpuHealth cpuHealth) {
        this.cpuHealth = cpuHealth;
    }


    public Map<String, GpuHealth> getGpuHealths() {
        return gpusHealth;
    }


    public void setGpuHealths(Map<String, GpuHealth> gpuHealths) {
        this.gpusHealth = gpuHealths;
    }


    public HealthData[] getHealthData() {
        return healthData;
    }


    public void setHealthData(HealthData[] healthDatas) {
        this.healthData = healthDatas;
    }

}
