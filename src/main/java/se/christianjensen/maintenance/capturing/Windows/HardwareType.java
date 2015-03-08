package se.christianjensen.maintenance.capturing.Windows;


public enum HardwareType {
    Mainboard,
    SuperIO,
    CPU,
    RAM,
    GpuNvidia,
    GpuAti,
    TBalancer,
    Heatmaster,
    HDD;

    static HardwareType fromOHMHwType(openhardwaremonitor.hardware.HardwareType hardwareType) {
        return HardwareType.valueOf(hardwareType.toString());
    }
}
