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
package com.krillsson.sysapi.core.metrics.defaultimpl;

import com.krillsson.sysapi.core.metrics.NetworkMetrics;
import com.krillsson.sysapi.core.SpeedMeasurementManager;
import com.krillsson.sysapi.core.domain.network.NetworkInterface;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceMetrics;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceSpeed;
import org.slf4j.Logger;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.net.SocketException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultNetworkMetrics implements NetworkMetrics {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultNetworkMetrics.class);

    private static final int BYTE_TO_BIT = 8;
    protected static final NetworkInterfaceSpeed EMPTY_INTERFACE_SPEED = new NetworkInterfaceSpeed(0, 0);

    private final HardwareAbstractionLayer hal;
    private final SpeedMeasurementManager speedMeasurementManager;

    protected DefaultNetworkMetrics(HardwareAbstractionLayer hal, SpeedMeasurementManager speedMeasurementManager) {
        this.hal = hal;
        this.speedMeasurementManager = speedMeasurementManager;
    }

    void register() {
        List<SpeedMeasurementManager.SpeedSource> collect = Arrays.stream(hal.getNetworkIFs())
                .map(n -> new SpeedMeasurementManager.SpeedSource() {
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
                        //TODO: maybe it's good enough to do this in getCurrentRead since getCurrentWrite is called immediately after
                        n.updateNetworkStats();
                        return n.getBytesSent() * BYTE_TO_BIT;
                    }
                })
                .collect(Collectors.toList());
        speedMeasurementManager.register(collect);
    }

    @Override
    public List<NetworkInterface> networkInterfaces() {
        return Arrays.stream(hal.getNetworkIFs()).map(mapToNetworkInterface()).collect(Collectors.toList());
    }

    @Override
    public Optional<NetworkInterface> networkInterfaceById(String id) {
        return Arrays.stream(hal.getNetworkIFs()).filter(n -> n.getName().equalsIgnoreCase(id)).map(
                mapToNetworkInterface()).findAny();
    }

    @Override
    public List<NetworkInterfaceLoad> networkInterfaceLoads() {
        return Arrays.stream(hal.getNetworkIFs())
                .map(mapToLoad())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NetworkInterfaceLoad> networkInterfaceLoadById(String id) {
        return Arrays.stream(hal.getNetworkIFs())
                .filter(n -> n.getName().equalsIgnoreCase(id))
                .map(mapToLoad())
                .findAny();
    }


    protected NetworkInterfaceSpeed speedForInterfaceWithName(String name) {
        Optional<SpeedMeasurementManager.CurrentSpeed> currentSpeedForName = speedMeasurementManager.getCurrentSpeedForName(
                name);
        return currentSpeedForName.map(s -> new NetworkInterfaceSpeed(
                s.getReadPerSeconds(),
                s.getWritePerSeconds()
        )).orElse(EMPTY_INTERFACE_SPEED);
    }


    Function<NetworkIF, NetworkInterface> mapToNetworkInterface() {
        return nic -> {
            boolean loopback = false;
            try {
                loopback = nic.getNetworkInterface().isLoopback();
            } catch (SocketException e) {
                //ignore
                LOGGER.warn("Socket exception while queering for loopback parameter", e);
            }
            return new NetworkInterface(
                    nic.getName(),
                    nic.getDisplayName(),
                    nic.getMacaddr(),
                    nic.getMTU(),
                    loopback,
                    nic.getIPv4addr(),
                    nic.getIPv6addr()
            );
        };
    }

    Function<NetworkIF, NetworkInterfaceLoad> mapToLoad() {
        return n -> new NetworkInterfaceLoad(
                new NetworkInterfaceMetrics(
                        n.getSpeed(),
                        n.getBytesRecv(),
                        n.getBytesSent(),
                        n.getPacketsRecv(),
                        n.getPacketsSent(),
                        n.getInErrors(),
                        n.getOutErrors()
                ),
                speedForInterfaceWithName(n.getName())
        );
    }
}
