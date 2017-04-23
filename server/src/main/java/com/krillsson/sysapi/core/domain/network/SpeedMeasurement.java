package com.krillsson.sysapi.core.domain.network;

import java.time.LocalDateTime;

public class SpeedMeasurement
{
    private final long read, write;
    private final LocalDateTime sampledAt;

    public SpeedMeasurement(long read, long write, LocalDateTime sampledAt)
    {
        this.read = read;
        this.write = write;
        this.sampledAt = sampledAt;
    }

    public long getRead()
    {
        return read;
    }

    public long getWrite()
    {
        return write;
    }

    public LocalDateTime getSampledAt()
    {
        return sampledAt;
    }
}
