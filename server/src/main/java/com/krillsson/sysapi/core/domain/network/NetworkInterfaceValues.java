package com.krillsson.sysapi.core.domain.network;

public class NetworkInterfaceValues {
    private final long speed;
    private final long bytesReceived;
    private final long bytesSent;
    private final long packetsReceived;
    private final long packetsSent;
    private final long inErrors;
    private final long outErrors;

    public NetworkInterfaceValues(long speed, long bytesReceived, long bytesSent, long packetsReceived, long packetsSent, long inErrors, long outErrors) {
        this.bytesReceived = bytesReceived;
        this.bytesSent = bytesSent;
        this.packetsReceived = packetsReceived;
        this.packetsSent = packetsSent;
        this.inErrors = inErrors;
        this.outErrors = outErrors;
        this.speed = speed;
    }

    public long getSpeed() {
        return speed;
    }

    public long getBytesReceived() {
        return bytesReceived;
    }

    public long getBytesSent() {
        return bytesSent;
    }

    public long getPacketsReceived() {
        return packetsReceived;
    }

    public long getPacketsSent() {
        return packetsSent;
    }

    public long getInErrors() {
        return inErrors;
    }

    public long getOutErrors() {
        return outErrors;
    }
}
