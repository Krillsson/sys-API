package se.christianjensen.maintenance.capturing.Windows;

import openhardwaremonitor.hardware.Computer;
import openhardwaremonitor.hardware.IHardware;
import se.christianjensen.maintenance.capturing.Cpu.Cpu;
import se.christianjensen.maintenance.capturing.Gpu.Gpu;
import se.christianjensen.maintenance.capturing.InformationProviderInterface;
import se.christianjensen.maintenance.representation.filesystem.FileSystem;
import se.christianjensen.maintenance.representation.memory.MainMemory;
import se.christianjensen.maintenance.representation.network.NetworkInfo;
import se.christianjensen.maintenance.representation.system.OperatingSystem;
import se.christianjensen.maintenance.sigar.SigarMetrics;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        List<IHardware> cpus = Arrays.asList(computer.getHardware())
                .stream()
                .filter(h -> (HardwareType.fromOHMHwType(h.getHardwareType()) == HardwareType.CPU))
                .collect(Collectors.toList());

        return null;
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
}
