package com.krillsson.sysapi.core.metrics;

import com.krillsson.sysapi.core.domain.storage.DiskInfo;
import com.krillsson.sysapi.core.domain.storage.DiskLoad;
import com.krillsson.sysapi.core.domain.storage.StorageInfo;

import java.util.List;
import java.util.Optional;

public interface DiskInfoProvider {
    List<DiskInfo> diskInfos();

    List<DiskLoad> diskLoads();

    Optional<DiskLoad> diskLoadByName(String name);

    Optional<DiskInfo> diskInfoByName(String name);
}
