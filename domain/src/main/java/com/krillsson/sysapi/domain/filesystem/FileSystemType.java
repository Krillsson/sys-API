package com.krillsson.sysapi.domain.filesystem;

public enum FileSystemType
{
    // Ordered so that ordinals match the constants
    // in org.hyperic.sigar.FileSystem
    Unknown, None, LocalDisk, Network, Ramdisk, Cdrom, Swap
}
