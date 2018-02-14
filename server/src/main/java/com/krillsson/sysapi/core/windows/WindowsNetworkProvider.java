package com.krillsson.sysapi.core.windows;

import com.krillsson.sysapi.core.DefaultNetworkProvider;
import com.krillsson.sysapi.core.SpeedMeasurementManager;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed;
import ohmwrapper.MonitorManager;
import ohmwrapper.NetworkMonitor;
import ohmwrapper.NicInfo;
import org.slf4j.Logger;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

public class WindowsNetworkProvider extends DefaultNetworkProvider {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WindowsNetworkProvider.class);


    private MonitorManager monitorManager;
    private HardwareAbstractionLayer hal;


    public WindowsNetworkProvider(HardwareAbstractionLayer hal, SpeedMeasurementManager speedMeasurementManager) {
        super(hal, speedMeasurementManager);
        this.hal = hal;
    }

    @Override
    public Optional<NetworkInterfaceSpeed> getSpeed(String id)
    {
        Optional<NetworkIF> networkOptional = Arrays.stream(hal.getNetworkIFs()).filter(n -> id.equals(n.getName())).findAny();
        if(!networkOptional.isPresent()){
            throw new NoSuchElementException(String.format("No NIC with id %s was found", id));
        }
        NetworkIF networkIF = networkOptional.get();

        monitorManager.Update();
        NetworkMonitor networkMonitor = monitorManager.getNetworkMonitor();
        NicInfo[] nics = networkMonitor.getNics();
        Optional<NicInfo> nicInfoOptional = Arrays.stream(nics).filter(n -> networkIF.getMacaddr().equals(n.getPhysicalAddress())).findAny();

        if(!nicInfoOptional.isPresent()){
            LOGGER.warn("Unable to find any OHM NicInfo matching with HW Address of {} for NIC {}. Defaulting to speed measurement.", networkIF.getMacaddr(), networkIF.getName());
            return super.getSpeed(id);
        }
        NicInfo nicInfo = nicInfoOptional.get();
        return Optional.of(new NetworkInterfaceSpeed((long) (nicInfo.getInBandwidth().getValue() * 1000), (long) (nicInfo.getOutBandwidth().getValue() * 1000)));
    }

    @Override
    protected void register() {
        // don't
    }

    public void setMonitorManager(MonitorManager monitorManager) {
        this.monitorManager = monitorManager;
    }
}
