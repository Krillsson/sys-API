package com.krillsson.sysapi.extension;

import com.krillsson.sysapi.domain.storage.HWDiskHealth;
import oshi.json.hardware.HWDiskStore;

public class DefaultInfoProvider extends InfoProviderBase implements InfoProvider {
    @Override
    protected boolean canProvide() {
        return true;
    }

    @Override
    public HWDiskHealth provideDiskHealth(String name, HWDiskStore diskStore) {
        return null;
    }
}
