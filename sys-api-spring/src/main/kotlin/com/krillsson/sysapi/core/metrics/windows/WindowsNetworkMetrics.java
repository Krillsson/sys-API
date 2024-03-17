/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */
package com.krillsson.sysapi.core.metrics.windows;

import com.krillsson.sysapi.core.connectivity.ConnectivityCheckManager;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed;
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultNetworkMetrics;
import com.krillsson.sysapi.core.metrics.defaultimpl.NetworkUploadDownloadRateMeasurementManager;
import com.krillsson.sysapi.periodictasks.TaskManager;
import ohmwrapper.NetworkMonitor;
import ohmwrapper.NicInfo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

public class WindowsNetworkMetrics extends DefaultNetworkMetrics {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WindowsNetworkMetrics.class);

    private DelegatingOHMManager monitorManager;
    private HardwareAbstractionLayer hal;


    public WindowsNetworkMetrics(TaskManager taskManager, HardwareAbstractionLayer hal, NetworkUploadDownloadRateMeasurementManager speedMeasurementManager, ConnectivityCheckManager connectivityCheckManager, DelegatingOHMManager monitorManager) {
        super(hal, speedMeasurementManager, connectivityCheckManager);
        this.hal = hal;
        this.monitorManager = monitorManager;
    }

    @NotNull
    @Override
    protected NetworkInterfaceSpeed speedForInterfaceWithName(@NotNull String name) {
        Optional<NetworkIF> networkOptional = hal.getNetworkIFs().stream()
                .filter(n -> name.equals(n.getName()))
                .findAny();
        if (!networkOptional.isPresent()) {
            throw new NoSuchElementException(String.format("No NIC with id %s was found", name));
        }
        NetworkIF networkIF = networkOptional.get();

        monitorManager.update();
        NetworkMonitor networkMonitor = monitorManager.getNetworkMonitor();
        NicInfo[] nics = networkMonitor.getNics();
        Optional<NicInfo> nicInfoOptional = Arrays.stream(nics)
                .filter(n -> networkIF.getMacaddr().equalsIgnoreCase(n.getPhysicalAddress()))
                .findAny();

        if (!nicInfoOptional.isPresent()) {
            return EMPTY_INTERFACE_SPEED;
        }
        NicInfo nicInfo = nicInfoOptional.get();
        return new NetworkInterfaceSpeed(
                (long) (nicInfo.getInBandwidth().getValue()),
                (long) (nicInfo.getOutBandwidth().getValue())
        );
    }
}
