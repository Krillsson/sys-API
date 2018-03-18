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
package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.network.NetworkInterfaceData;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed;
import org.slf4j.Logger;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultNetworkProvider implements NetworkInfoProvider{

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultNetworkProvider.class);

    private static final int BYTE_TO_BIT = 8;
    protected static final NetworkInterfaceSpeed EMPTY_INTERFACE_SPEED = new NetworkInterfaceSpeed(0, 0);

    private final HardwareAbstractionLayer hal;
    private final SpeedMeasurementManager speedMeasurementManager;

    protected DefaultNetworkProvider(HardwareAbstractionLayer hal, SpeedMeasurementManager speedMeasurementManager) {
        this.hal = hal;
        this.speedMeasurementManager = speedMeasurementManager;
    }

    public void register() {
        List<SpeedMeasurementManager.SpeedSource> collect = Arrays.stream(hal.getNetworkIFs()).map(n -> new SpeedMeasurementManager.SpeedSource() {
            @Override
            public String getName() {
                return n.getName();
            }

            @Override
            public long getCurrentRead() {
                n.updateNetworkStats();
                return n.getBytesRecv() * BYTE_TO_BIT;
            }

            @Override
            public long getCurrentWrite() {
                n.updateNetworkStats();
                return n.getBytesSent() * BYTE_TO_BIT;
            }
        }).collect(Collectors.toList());
        speedMeasurementManager.register(collect);
    }

    @Override
    public String[] getNetworkInterfaceNames(){
        return Arrays.stream(hal.getNetworkIFs()).map(NetworkIF::getName).toArray(String[]::new);
    }

    @Override
    public NetworkInterfaceData[] getAllNetworkInterfaces() {
        return Arrays.stream(hal.getNetworkIFs()).map(n -> new NetworkInterfaceData(n, getSpeed(n.getName()))).toArray(NetworkInterfaceData[]::new);
    }

    @Override
    public Optional<NetworkInterfaceData> getNetworkInterfaceById(String id) {
        return Arrays.stream(hal.getNetworkIFs()).filter(n -> id.equals(n.getName())).map(nic -> new NetworkInterfaceData(nic, getSpeed(nic.getName()))).findFirst();
    }

    protected NetworkInterfaceSpeed getSpeed(String id) {
        Optional<NetworkIF> networkOptional = Arrays.stream(hal.getNetworkIFs()).filter(n -> id.equals(n.getName())).findAny();
        if (networkOptional.isPresent()) {
            NetworkIF networkIF = networkOptional.get();
            return networkInterfaceSpeed(networkIF.getName());
        } else {
            return EMPTY_INTERFACE_SPEED;
        }
    }

    private NetworkInterfaceSpeed networkInterfaceSpeed(String name){
        SpeedMeasurementManager.CurrentSpeed currentSpeedForName = speedMeasurementManager.getCurrentSpeedForName(name);
        return new NetworkInterfaceSpeed(currentSpeedForName.getReadPerSeconds(), currentSpeedForName.getWritePerSeconds());
    }

}
