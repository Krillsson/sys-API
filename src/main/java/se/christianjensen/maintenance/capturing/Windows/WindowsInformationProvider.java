package se.christianjensen.maintenance.capturing.Windows;

import openhardwaremonitor.hardware.Computer;
import openhardwaremonitor.hardware.IHardware;
import openhardwaremonitor.hardware.ISensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.christianjensen.maintenance.capturing.Cpu.Cpu;
import se.christianjensen.maintenance.capturing.Cpu.CpuCore;
import se.christianjensen.maintenance.capturing.Gpu.Gpu;
import se.christianjensen.maintenance.capturing.InformationProvider;
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

public class WindowsInformationProvider implements InformationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowsInformationProvider.class);


    SigarMetrics sigarMetrics;
    Computer computer;
    float cachedFanRpm = 0f;

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
        List<IHardware> hardware = Arrays.asList(computer.getHardware());
        return getValuesFromOhmCpu(hardware);
    }

    @Override
    public List<Gpu> getGpus() {
        List<IHardware> hardware = Arrays.asList(computer.getHardware());
        return getValuesFromOhmGpu(hardware);
    }

    private List<Gpu> getValuesFromOhmGpu(List<IHardware> hardware) {
        List<Gpu> result = new ArrayList<>();
        List<IHardware> ohmGpus = hardware
                .stream()
                .filter(h -> (HardwareType.fromOHMHwType(h.getHardwareType()) == HardwareType.GpuNvidia
                        || (HardwareType.fromOHMHwType(h.getHardwareType()) == HardwareType.GpuAti)))
                .collect(Collectors.toList());
        for (IHardware ohmGpu : ohmGpus) {
            ohmGpu.Update();
            List<ISensor> sensorStream = Arrays.asList(ohmGpu.getSensors());
            Gpu gpu = new Gpu();
            gpu.setName(ohmGpu.getName());
            gpu.setLoad(sensorStream.stream().filter(g -> SensorType.fromOHMSensorType(g.getSensorType()) == SensorType.Load && g.getName().contains("Core")).findFirst().map(ISensor::getValue).orElse(0f));
            gpu.setFanRpm(sensorStream.stream().filter(g -> SensorType.fromOHMSensorType(g.getSensorType()) == SensorType.Fan).findFirst().map(ISensor::getValue).orElse(0f));
            gpu.setTemp(sensorStream.stream().filter(g -> SensorType.fromOHMSensorType(g.getSensorType()) == SensorType.Temperature).findFirst().map(ISensor::getValue).orElse(0f));
            gpu.setClock(sensorStream.stream().filter(g -> SensorType.fromOHMSensorType(g.getSensorType()) == SensorType.Clock && g.getName().contains("Core")).findFirst().map(ISensor::getValue).orElse(0f));
            gpu.setMemoryUsed(sensorStream.stream().filter(g -> SensorType.fromOHMSensorType(g.getSensorType()) == SensorType.Load && g.getName().contains("Memory") && !g.getName().contains("Controller")).findFirst().map(ISensor::getValue).orElse(0f));
            result.add(gpu);
        }
        return result;
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


    private List<Cpu> getValuesFromOhmCpu(List<IHardware> hardware) {
        List<Cpu> result = new ArrayList<>();
        List<IHardware> ohmCpus = hardware
                .stream()
                .filter(h -> (HardwareType.fromOHMHwType(h.getHardwareType()) == HardwareType.CPU))
                .collect(Collectors.toList());
        IHardware mainBoard = hardware
                .stream()
                .filter(h -> (HardwareType.fromOHMHwType(h.getHardwareType()) == HardwareType.Mainboard))
                .findFirst()
                .orElse(null);

        for (IHardware ohmcpu : ohmCpus) {
            ohmcpu.Update();
            Cpu cpu = new Cpu();
            cpu.setName(ohmcpu.getName());
            cpu.setCores(new ArrayList<>());
            int cores = Arrays.asList(ohmcpu.getSensors()).stream().mapToInt(ISensor::getIndex).max().getAsInt();
            for (int i = 1; i <= cores; i++) {
                final int finalI = i;
                List<ISensor> coreSensors = Arrays.asList(ohmcpu.getSensors()).stream().filter(s -> s.getName().contains("Core") && s.getName().contains(String.valueOf(finalI))).collect(Collectors.toList());
                CpuCore cpuCore = new CpuCore();
                for (ISensor coreSensor : coreSensors) {
                    populateCore(coreSensor, cpuCore);
                }
                cpu.getCores().add(cpuCore);
            }
            cpu.setLoad(Arrays.asList(ohmcpu.getSensors()).stream().filter(s -> s.getName().contains("CPU Total")).findFirst().map(ISensor::getValue).orElse(0f));
            cpu.setFanRpm(getCpuRpm(mainBoard));
            cpu.setAverageTemp((float) cpu.getCores().stream().mapToDouble(c -> (double) c.getTemp()).average().getAsDouble());
            result.add(cpu);
        }
        return result;
    }

    private float getCpuRpm(IHardware mainBoard) {
        LOGGER.debug("Attempting to resolve Cpufan RPM");
        float result = 0f;
        if (mainBoard != null) {
            mainBoard.Update();
            for (IHardware subMainBoard : mainBoard.getSubHardware()) {
                subMainBoard.Update();
                result = Arrays.asList(subMainBoard.getSensors())
                        .stream()
                        .filter(s -> s.getName().contains("CPU Fan"))
                        .findFirst()
                        .map(ISensor::getValue)
                        .orElse(0f);
            }
        }
        return result;
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
