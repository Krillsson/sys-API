package se.christianjensen.maintenance.capturing;

import se.christianjensen.maintenance.capturing.Cpu.Cpu;
import se.christianjensen.maintenance.capturing.Gpu.Gpu;
import se.christianjensen.maintenance.representation.filesystem.FileSystem;
import se.christianjensen.maintenance.representation.memory.MainMemory;
import se.christianjensen.maintenance.representation.network.NetworkInfo;
import se.christianjensen.maintenance.representation.system.OperatingSystem;

import java.util.List;

public interface InformationProvider {
    List<FileSystem> getFilesystems();

    List<Cpu> getCpus();

    List<Gpu> getGpus();

    MainMemory getMainMemory();

    NetworkInfo getNetworkInfo();

    OperatingSystem getOperatingSystem();
}
