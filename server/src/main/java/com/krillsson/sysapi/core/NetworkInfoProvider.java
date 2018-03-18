package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.network.NetworkInterfaceData;

import java.util.Optional;

public interface NetworkInfoProvider {
    NetworkInterfaceData[] getAllNetworkInterfaces();
    Optional<NetworkInterfaceData> getNetworkInterfaceById(String id);
    String[] getNetworkInterfaceNames();
}
