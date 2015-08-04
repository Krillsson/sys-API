package com.krillsson.sysapi.representation.filesystem;

public enum FSType {
    // Ordered so that ordinals match the constants
    // in org.hyperic.sigar.FileSystem
    Unknown, None, LocalDisk, Network, Ramdisk, Cdrom, Swap
}
