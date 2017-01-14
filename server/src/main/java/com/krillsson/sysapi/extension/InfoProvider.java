package com.krillsson.sysapi.extension;

import com.krillsson.sysapi.domain.storage.HWDiskHealth;
import oshi.json.hardware.HWDiskStore;

public interface InfoProvider {
    HWDiskHealth provideDiskHealth(String name, HWDiskStore diskStore);
}
