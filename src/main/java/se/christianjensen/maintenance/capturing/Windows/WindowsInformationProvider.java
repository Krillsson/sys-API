package se.christianjensen.maintenance.capturing.Windows;

import openhardwaremonitor.hardware.Computer;
import openhardwaremonitor.hardware.IHardware;
import openhardwaremonitor.hardware.ISensor;
import se.christianjensen.maintenance.capturing.Cpu.Cpu;
import se.christianjensen.maintenance.capturing.Cpu.CpuCore;
import se.christianjensen.maintenance.capturing.Gpu.Gpu;
import se.christianjensen.maintenance.capturing.InformationProviderInterface;
import se.christianjensen.maintenance.representation.filesystem.FileSystem;
import se.christianjensen.maintenance.representation.memory.MainMemory;
import se.christianjensen.maintenance.representation.network.NetworkInfo;
import se.christianjensen.maintenance.representation.system.OperatingSystem;
import se.christianjensen.maintenance.sigar.SigarMetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static se.christianjensen.maintenance.capturing.Windows.SensorType.fromOHMSensorType;

public class WindowsInformationProvider implements InformationProviderInterface {
    SigarMetrics sigarMetrics;
    Computer computer;

    public WindowsInformationProvider(SigarMetrics sigarMetrics, Computer computer) {
        this.sigarMetrics = sigarMetrics;
        this.computer = computer;

        computer.setCPUEnabled(true);
        computer.setFanControllerEnabled(true);
        computer.setHDDEnabled(true);
        computer.setMainboardEnabled(true);
        computer.setGPUEnabled(true);
        computer.setRAMEnabled(true);
        computer.Open();
    }

    @Override
    public List<FileSystem> getFilesystems() {
        return null;
    }

    @Override
    public List<Cpu> getCpus() {
        List<Cpu> result = new ArrayList<Cpu>();
        List<IHardware> ohmcpus = Arrays.asList(computer.getHardware())
                .stream()
                .filter(h -> (HardwareType.fromOHMHwType(h.getHardwareType()) == HardwareType.CPU))
                .collect(Collectors.toList());
        for (IHardware ohmcpu : ohmcpus) {
            result.add(getValuesFromOhmCpu(ohmcpu));
        }
        return result;
    }

    @Override
    public List<Gpu> getGpus() {
        return null;
    }

    @Override
    public MainMemory getMainMemory() {
        return null;
    }

    @Override
    public NetworkInfo getNetworkInfo() {
        return null;
    }

    @Override
    public OperatingSystem getOperatingSystem() {
        return null;
    }


    private Cpu getValuesFromOhmCpu(IHardware ohmcpu) {
        ohmcpu.Update();
        Cpu cpu = new Cpu();
        cpu.setName(ohmcpu.getName());
        cpu.setCores(new ArrayList<>());
        int cores = Arrays.asList(ohmcpu.getSensors()).stream().mapToInt(ISensor::getIndex).max().getAsInt();
        for (int i = 0; i <= cores; i++) {
            final int finalI = i;
            List<ISensor> coreSensors = Arrays.asList(ohmcpu.getSensors()).stream().filter(s -> s.getName().contains("Core") && s.getName().contains(String.valueOf(finalI))).collect(Collectors.toList());
            CpuCore cpuCore = new CpuCore();
            for (ISensor coreSensor : coreSensors) {
                populateCore(coreSensor, cpuCore);
            }
            cpu.getCores().add(cpuCore);
        }
        return cpu;
    }

    private void populateCore(ISensor ohmSensor, CpuCore core) {
        switch (fromOHMSensorType(ohmSensor.getSensorType())) {
            case Clock:
                core.setClock(ohmSensor.getValue());
                break;
            case Temperature:
                core.setTemp(ohmSensor.getValue());
                break;
            case Load:
                core.setLoad(ohmSensor.getValue());
                break;
            default:
                break;
        }
    }
}
