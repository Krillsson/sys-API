package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.core.domain.drives.Drive;
import com.krillsson.sysapi.core.domain.drives.DriveLoad;

import java.util.List;
import java.util.Optional;

public interface DriveMetrics {
    List<Drive> drives();

    List<DriveLoad> driveLoads();

    Optional<Drive> driveByName(String name);

    Optional<DriveLoad> driveLoadByName(String name);
}
