package com.krillsson.sysapi.dto.network;

public class NetworkInterfaceSpeed {
    private long receiveBytesPerSecond;
    private long sendBytesPerSecond;

    public NetworkInterfaceSpeed(long receiveBytesPerSecond, long sendBytesPerSecond) {
        this.receiveBytesPerSecond = receiveBytesPerSecond;
        this.sendBytesPerSecond = sendBytesPerSecond;
    }

    public NetworkInterfaceSpeed() {
    }

    public long getReceiveBytesPerSecond() {
        return receiveBytesPerSecond;
    }

    public long getSendBytesPerSecond() {
        return sendBytesPerSecond;
    }

    public void setReceiveBytesPerSecond(long receiveBytesPerSecond) {
        this.receiveBytesPerSecond = receiveBytesPerSecond;
    }

    public void setSendBytesPerSecond(long sendBytesPerSecond) {
        this.sendBytesPerSecond = sendBytesPerSecond;
    }
}
