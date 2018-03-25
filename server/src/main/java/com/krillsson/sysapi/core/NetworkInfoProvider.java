package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.network.NetworkInterface;
import com.krillsson.sysapi.core.domain.network.NetworkInterfaceLoad;

import java.util.List;
import java.util.Optional;

public interface NetworkInfoProvider {
    List<NetworkInterface> networkInterfaces();

    Optional<NetworkInterface> networkInterfaceById(String id);

    List<NetworkInterfaceLoad> networkInterfaceLoads();

    Optional<NetworkInterfaceLoad> networkInterfaceLoadById(String id);
}
