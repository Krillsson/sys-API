package com.krillsson.sysapi.core.domain.network;

public class NetworkInterfaceSpeedMeasurement
{
    private final long rxBytes, txBytes;
    private final long sampledAt;

    public NetworkInterfaceSpeedMeasurement(long rxBytes, long txBytes, long sampledAt)
    {
        this.rxBytes = rxBytes;
        this.txBytes = txBytes;
        this.sampledAt = sampledAt;
    }

    public long getRxBytes()
    {
        return rxBytes;
    }

    public long getTxBytes()
    {
        return txBytes;
    }

    public long getSampledAt()
    {
        return sampledAt;
    }
}
