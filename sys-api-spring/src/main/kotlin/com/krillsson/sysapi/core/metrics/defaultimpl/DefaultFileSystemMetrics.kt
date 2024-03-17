package com.krillsson.sysapi.core.metrics.defaultimpl

import com.krillsson.sysapi.core.domain.filesystem.FileSystem
import com.krillsson.sysapi.core.domain.filesystem.FileSystemLoad
import com.krillsson.sysapi.core.metrics.FileSystemMetrics
import com.krillsson.sysapi.util.asHex
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
            .distinctBy { it.id }
    }

    override fun fileSystemById(id: String): FileSystem? {
        return operatingSystem.fileSystem
            .fileStores
            .map { it.asFileSystem() }
            .firstOrNull { it.id.equals(id, ignoreCase = true) }
    }

    override fun fileSystemLoads(): List<FileSystemLoad> {
        return operatingSystem.fileSystem
            .fileStores.map {
                it.asFileSystemLoad()
            }
            .distinctBy { it.id }
    }

    override fun fileSystemLoadById(id: String): FileSystemLoad? {
        return operatingSystem.fileSystem
            .fileStores
            .map { it.asFileSystemLoad() }
            .firstOrNull { it.id.equals(id, ignoreCase = true) }
    }

    private fun OSFileStore.asFileSystemLoad(): FileSystemLoad {
        val id = if (uuid.isNullOrEmpty()) createFallbackUrl() else uuid
        return FileSystemLoad(
            name,
            id,
            freeSpace,
            usableSpace,
            totalSpace
        )
    }

    private fun OSFileStore.asFileSystem(): FileSystem {
        val id = if (uuid.isNullOrEmpty()) createFallbackUrl() else uuid
        return FileSystem(
            name,
            id,
            description,
            label,
            type,
            volume,
            mount,
            totalSpace
        )
    }

    private fun OSFileStore.createFallbackUrl(): String {
        return "fallback-id-${name.hashCode().asHex()}-${type.hashCode().asHex()}-${
            mount.hashCode().asHex()
        }-${volume.hashCode().asHex()}-${label.hashCode().asHex()}"
    }
}
