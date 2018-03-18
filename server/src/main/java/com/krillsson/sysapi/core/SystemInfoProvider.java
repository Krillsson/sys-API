package com.krillsson.sysapi.core;

import com.krillsson.sysapi.core.domain.system.SystemInfo;
import oshi.software.os.OperatingSystem;

public interface SystemInfoProvider {
    OperatingSystem operatingSystem();
    SystemInfo systemInfo();
}
