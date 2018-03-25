package com.krillsson.sysapi.core.domain.network;

public class NetworkInterfaceSpeed {
    private final long receiveBytesPerSecond;
    private final long sendBytesPerSecond;

    public NetworkInterfaceSpeed(long receiveBytesPerSecond, long sendBytesPerSecond) {
        this.receiveBytesPerSecond = receiveBytesPerSecond;
        this.sendBytesPerSecond = sendBytesPerSecond;
    }

    public long getReceiveBytesPerSecond() {
        return receiveBytesPerSecond;
    }

    public long getSendBytesPerSecond() {
        return sendBytesPerSecond;
    }
}
