package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.filesystem.FileSystem
import com.krillsson.sysapi.core.domain.filesystem.FileSystemLoad
import com.krillsson.sysapi.core.metrics.FileSystemMetrics
import oshi.software.os.OSFileStore
import oshi.software.os.OperatingSystem


open class DefaultFileSystemMetrics(
    private val operatingSystem: OperatingSystem
) : FileSystemMetrics {

    override fun fileSystems(): List<FileSystem> {
        return operatingSystem.fileSystem
            .fileStores.map {
                it.asFileSystem()
            }
    }

    override fun fileSystemByName(name: String): FileSystem? {
        return operatingSystem.fileSystem
            .fileStores
            .firstOrNull { it.name.equals(name, ignoreCase = true) }
            ?.asFileSystem()
    }

    override fun fileSystemLoads(): List<FileSystemLoad> {
        return operatingSystem.fileSystem
            .fileStores.map {
                it.asFileSystemLoad()
            }
    }

    override fun fileSystemLoadByName(name: String): FileSystemLoad? {
        return operatingSystem.fileSystem
            .fileStores
            .firstOrNull { it.name.equals(name, ignoreCase = true) }
            ?.asFileSystemLoad()
    }

    private fun OSFileStore.asFileSystemLoad(): FileSystemLoad{
        return FileSystemLoad(
            name,
            freeSpace,
            usableSpace,
            totalSpace
        )
    }

    private fun OSFileStore.asFileSystem(): FileSystem {
        return FileSystem(
            name,
            description,
            label,
            type,
            volume,
            mount
        )
    }
}