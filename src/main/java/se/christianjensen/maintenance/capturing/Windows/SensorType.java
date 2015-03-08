package se.christianjensen.maintenance.capturing.Windows;

public enum SensorType {
    Voltage, // V
    Clock, // MHz
    Temperature, // °C
    Load, // %
    Fan, // RPM
    Flow, // L/h
    Control, // %
    Level, // %
    Factor, // 1
    Power, // W
    Data; // GB = 2^30 Bytes

    public static SensorType fromOHMSensorType(openhardwaremonitor.hardware.SensorType sensorType) {
        return SensorType.valueOf(sensorType.toString());
    }
}
