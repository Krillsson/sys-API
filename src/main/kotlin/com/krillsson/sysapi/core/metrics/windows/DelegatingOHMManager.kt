package com.krillsson.sysapi.core.metrics.windows;

import ohmwrapper.*;

public class DelegatingOHMManager {
    private final MonitorManager monitorManager;

    public DelegatingOHMManager(MonitorManager monitorManager) {
        this.monitorManager = monitorManager;
    }

    public OHMMonitor[] OHMMonitors() {
        return monitorManager.OHMMonitors();
    }

    public GpuMonitor[] GpuMonitors() {
        return monitorManager.GpuMonitors();
    }

    public CpuMonitor[] CpuMonitors() {
        return monitorManager.CpuMonitors();
    }

    public DriveMonitor[] DriveMonitors() {
        return monitorManager.DriveMonitors();
    }

    public MainboardMonitor getMainboardMonitor() {
        return monitorManager.getMainboardMonitor();
    }

    public RamMonitor getRamMonitor() {
        return monitorManager.getRamMonitor();
    }

    public NetworkMonitor getNetworkMonitor() {
        return monitorManager.getNetworkMonitor();
    }

    public void update() {
        monitorManager.Update();
    }
}
