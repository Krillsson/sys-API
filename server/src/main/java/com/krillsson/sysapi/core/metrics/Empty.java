package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.core.domain.disk.DiskSpeed;
import com.krillsson.sysapi.core.domain.drives.*;

import java.util.Collections;
import java.util.UUID;

public class Empty {
    public static final DriveHealth DRIVE_HEALTH = new DriveHealth(-1, Collections.emptyList());
    public static final DriveSpeed DRIVE_SPEED = new DriveSpeed(-1, -1);
    public static final DiskSpeed DISK_SPEED = new DiskSpeed(-1, -1);
    public static final DriveLoad DRIVE_LOAD = new DriveLoad(
            "N/A",
            "N/A", new DriveValues(-1, -1, -1, -1, -1, -1, -1, -1),
            DRIVE_SPEED,
            DRIVE_HEALTH
    );
    public static final OsPartition OS_PARTITION = new OsPartition(
            "n/a",
            "n/a",
            "n/a",
            UUID.randomUUID().toString(),
            0,
            0,
            0,
            "n/a",
            "n/a",
            "n/a",
            "n/a",
            "n/a",
            0,
            0

    );
}
