package com.krillsson.sysapi.core.domain.network;

import oshi.hardware.NetworkIF;

public class NetworkInterfaceData
{
    private final NetworkIF networkIF;
    private final NetworkInterfaceSpeed networkInterfaceSpeed;

    public NetworkInterfaceData(NetworkIF networkIF, NetworkInterfaceSpeed networkInterfaceSpeed)
    {
        this.networkIF = networkIF;
        this.networkInterfaceSpeed = networkInterfaceSpeed;
    }

    public NetworkIF getNetworkIF()
    {
        return networkIF;
    }

    public NetworkInterfaceSpeed getNetworkInterfaceSpeed()
    {
        return networkInterfaceSpeed;
    }
}
