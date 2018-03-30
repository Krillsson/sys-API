package com.krillsson.sysapi.dto.network;

public class NetworkInterfaceValues {
    private long speed;
    private long bytesReceived;
    private long bytesSent;
    private long packetsReceived;
    private long packetsSent;
    private long inErrors;
    private long outErrors;

    public NetworkInterfaceValues(long speed, long bytesReceived, long bytesSent, long packetsReceived, long packetsSent, long inErrors, long outErrors) {
        this.bytesReceived = bytesReceived;
        this.bytesSent = bytesSent;
        this.packetsReceived = packetsReceived;
        this.packetsSent = packetsSent;
        this.inErrors = inErrors;
        this.outErrors = outErrors;
        this.speed = speed;
    }

    public NetworkInterfaceValues() {
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

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public void setBytesReceived(long bytesReceived) {
        this.bytesReceived = bytesReceived;
    }

    public void setBytesSent(long bytesSent) {
        this.bytesSent = bytesSent;
    }

    public void setPacketsReceived(long packetsReceived) {
        this.packetsReceived = packetsReceived;
    }

    public void setPacketsSent(long packetsSent) {
        this.packetsSent = packetsSent;
    }

    public void setInErrors(long inErrors) {
        this.inErrors = inErrors;
    }

    public void setOutErrors(long outErrors) {
        this.outErrors = outErrors;
    }
}
