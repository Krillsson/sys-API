package com.krillsson.sysapi.domain.network;

import oshi.json.hardware.NetworkIF;

public class NetworkInterfacesData {
    private final NetworkIF[] networkIFs;
    private final long dataCapturedAt;

    public NetworkInterfacesData(NetworkIF[] networkIFs, long dataCapturedAt) {
        this.networkIFs = networkIFs;
        this.dataCapturedAt = dataCapturedAt;
    }

    public NetworkIF[] getNetworkIFs() {
        return networkIFs;
    }

    public long getDataCapturedAt() {
        return dataCapturedAt;
    }
}
