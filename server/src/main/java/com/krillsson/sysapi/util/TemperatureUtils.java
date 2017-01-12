package com.krillsson.sysapi.util;

import com.krillsson.sysapi.domain.Thermals;
import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Cpu;
import com.profesorfalken.wmi4java.WMI4Java;
import org.jutils.jhardware.util.HardwareInfoUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TemperatureUtils {
    private final OperatingSystem currentOperatingSystem;
    private static final String CPUTEMP_THERMAL_ROOT = "/sys/class/thermal/thermal_zone";
    private static final String CPUTEMP_THERMAL_FILE = "temp";
    private static final double[] NOT_DETECTED = new double[0];

    public TemperatureUtils(OperatingSystem currentOperatingSystem) {
        this.currentOperatingSystem = currentOperatingSystem;
    }

    public Thermals getCpuThermals() {


        switch (currentOperatingSystem) {
            case WINDOWS:
                return getCpuTemperatureForWindows();
            case LINUX:
                return getCpuTemperatureForLinux();
            case MAC_OS:
                //https://github.com/Chris911/iStats
            case FREE_BSD:
            case UNKNOWN:
                break;
        }
        return new Thermals(NOT_DETECTED, NOT_DETECTED);
    }

    private Thermals getCpuThermalsByjSensors() {
        double[] temperatures = NOT_DETECTED;
        double[] fans = NOT_DETECTED;
        List cpus = JSensors.get.components().cpus;
        if (cpus.size() > 0) {
            Cpu cpu = (Cpu) cpus.get(0);
            if (cpu.sensors.temperatures != null && cpu.sensors.temperatures.size() > 0) {
                temperatures = new double[cpu.sensors.temperatures.size()];
                for (int i = 0; i < cpu.sensors.temperatures.size(); i++) {
                    temperatures[i] = cpu.sensors.temperatures.get(i).value;
                }
            }
            if (cpu.sensors.fans != null && cpu.sensors.fans.size() > 0) {
                fans = new double[cpu.sensors.fans.size()];
                for (int i = 0; i < cpu.sensors.temperatures.size(); i++) {
                    fans[i] = cpu.sensors.fans.get(i).value;
                }
            }
        }

        return new Thermals(temperatures, fans);
    }

    private Thermals getCpuTemperatureForWindows() {
        Thermals thermals = getCpuThermalsByjSensors();
        if (Arrays.equals(NOT_DETECTED, thermals.getTemperatures())) {
            Map temperatureDataMap = WMI4Java.get().VBSEngine().namespace("root/wmi").getWMIObject("MSAcpi_ThermalZoneTemperature");
            if (temperatureDataMap.containsKey("CurrentTemperature")) {
                thermals.setTemperatures(new double[]{Double.valueOf((String) temperatureDataMap.get("CurrentTemperature"))/ 10 - 273});
            }
        }

        if (Arrays.equals(NOT_DETECTED, thermals.getFans()) && false) {
            //https://msdn.microsoft.com/en-us/library/aa394146(v=vs.85).aspx
            Map temperatureDataMap = WMI4Java.get().VBSEngine().namespace("root/wmi").getWMIObject("MSAcpi_ThermalZoneTemperature");
            if (temperatureDataMap.containsKey("CurrentTemperature")) {
                thermals.setTemperatures(new double[]{Double.valueOf((String) temperatureDataMap.get("CurrentTemperature"))/ 10 - 273});
            }
        }

        return thermals;
    }

    private Thermals getCpuTemperatureForLinux() {
        Thermals thermals = getCpuThermalsByjSensors();
        if (Arrays.equals(NOT_DETECTED, thermals.getTemperatures())) {
            int sensorIndex = 0;

            while (true) {
                if (!(new File("/sys/class/thermal/thermal_zone" + sensorIndex)).exists()) {
                    return thermals;
                }

                String tempFile = "/sys/class/thermal/thermal_zone" + sensorIndex++ + "/" + "temp";
                String value = HardwareInfoUtils.getSingleValueFromFile(tempFile);
                if (value != null && !value.isEmpty()) {
                    thermals.setTemperatures(new double[]{Double.valueOf(value.trim()).intValue() / 1000});
                    break;
                }
            }
        }

        return thermals;
    }
}
