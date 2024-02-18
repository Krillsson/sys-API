package com.krillsson.sysapi.docker

import com.github.dockerjava.api.command.HealthState
import com.github.dockerjava.api.command.HealthStateLog
import com.github.dockerjava.api.model.*
import com.krillsson.sysapi.core.domain.docker.*
import com.krillsson.sysapi.core.domain.docker.Config
import com.krillsson.sysapi.core.domain.docker.Container
import com.krillsson.sysapi.core.domain.docker.HostConfig
import com.krillsson.sysapi.core.domain.docker.Link
import com.krillsson.sysapi.core.domain.docker.Mount
import com.krillsson.sysapi.core.domain.docker.Network
import com.krillsson.sysapi.core.domain.docker.PortBinding
import com.krillsson.sysapi.core.domain.docker.PortConfig
import com.krillsson.sysapi.core.domain.system.Platform
import java.time.Duration
import java.time.Instant

fun ContainerConfig.asConfig(volumeBindings: List<VolumeBinding>): Config {
    return Config(
        env?.asList().orEmpty(),
        volumeBindings,
        cmd?.asList().orEmpty(),
        exposedPorts.orEmpty().map { it.asPortConfig() }
    )
}

fun ExposedPort.asPortConfig(): PortConfig {
    return PortConfig(
        port,
        protocol.asPortProtocol()
    )
}

fun InternetProtocol.asPortProtocol() = when (this) {
    InternetProtocol.TCP -> PortProtocol.TCP
    InternetProtocol.UDP -> PortProtocol.UDP
    InternetProtocol.SCTP -> PortProtocol.SCTP
}

fun com.github.dockerjava.api.model.Container.asContainer(config: Config, health: Health?): Container {
    return Container(
        command.orNotApplicable(),
        created ?: 0L,
        hostConfig?.asHostConfig() ?: HostConfig("N/A"),
        config,
        id,
        image.orNotApplicable(),
        imageId.orNotApplicable(),
        labels.orEmpty(),
        mounts.asMounts(),
        names.toList(),
        networkSettings?.asNetworkSettings() ?: emptyList(),
        ports.asPorts(),
        sizeRootFs ?: 0L,
        sizeRw ?: 0L,
        State.fromString(state),
        health,
        status.orNotApplicable()
    )
}

fun Array<ContainerPort>.asPorts(): List<PortBinding> {
    return map {
        it.asPort()
    }
}

fun ContainerPort.asPort(): PortBinding {
    return PortBinding(ip.orNotApplicable(), privatePort ?: 0, publicPort ?: 0, type.orNotApplicable())
}

fun ContainerNetworkSettings.asNetworkSettings(): List<NetworkSetting> {
    return networks.orEmpty().map {
        NetworkSetting(it.key, it.value.asNetwork())
    }
}

fun ContainerNetwork.asNetwork(): Network {
    return Network(
        aliases.orEmpty(),
        endpointId.orNotApplicable(),
        gateway.orNotApplicable(),
        globalIPv6Address.orNotApplicable(),
        globalIPv6PrefixLen ?: 0,
        ipamConfig?.asIpam() ?: Ipam("N/A", "N/A"),
        ipAddress.orNotApplicable(),
        ipPrefixLen ?: 0,
        ipV6Gateway.orNotApplicable(),
        links?.asLinks().orEmpty(),
        macAddress.orNotApplicable(),
        networkID.orNotApplicable()
    )
}


fun Array<com.github.dockerjava.api.model.Link>.asLinks(): List<Link> {
    return map {
        it.asLink()
    }
}

fun com.github.dockerjava.api.model.Link.asLink(): Link {
    return Link(name, alias)
}

fun ContainerNetwork.Ipam.asIpam(): Ipam {
    return Ipam(
        ipv4Address,
        ipv6Address
    )
}

fun List<ContainerMount>.asMounts(): List<Mount> {
    return map {
        it.asMount()
    }
}

fun ContainerMount.asMount(): Mount {
    return Mount(
        destination.orNotApplicable(),
        driver.orNotApplicable(),
        mode.orNotApplicable(),
        name.orNotApplicable(),
        propagation.orNotApplicable(),
        rw ?: false,
        source.orNotApplicable()
    )
}

fun ContainerHostConfig.asHostConfig(): HostConfig {
    return HostConfig(
        networkMode
    )
}

fun Array<VolumeBind>.asVolumeBindings() = map {
    it.asVolumeBind()
}

fun VolumeBind.asVolumeBind() = VolumeBinding(
    hostPath, containerPath
)

fun String?.orNotApplicable() = this ?: "N/A"

fun HealthState.asHealth(): Health {
    return Health(
        status,
        failingStreak,
        log?.asHealthLogEntries().orEmpty()
    )
}

fun HealthStateLog.asHealthLogEntry() = HealthLogEntry(
    start,
    end,
    output,
    exitCodeLong
)

fun List<HealthStateLog>.asHealthLogEntries(): List<HealthLogEntry> {
    return map { it.asHealthLogEntry() }
}

fun Statistics.asStatistics(id: String, platform: Platform): ContainerMetrics {
    return ContainerMetrics(
        id = id,
        currentPid = pidsStats.current.orDefault(),
        cpuUsage = cpuStats.asCpuUsage(platform, preCpuStats, read, preread, numProcs),
        memoryUsage = memoryStats.asMemoryUsage(),
        networkUsage = networks.orEmpty().asNetworkUsage(),
        blockIOUsage = blkioStats.asBlockIOUsage()
    )
}

private fun Map<String, StatisticNetworksConfig>.asNetworkUsage(): NetworkUsage {
    var bytesTransferred: Long = 0
    var bytesReceived: Long = 0

    entries.forEach {
        bytesTransferred += it.value.txBytes.orDefault()
        bytesReceived += it.value.rxBytes.orDefault()
    }

    return NetworkUsage(
        bytesReceived = bytesReceived,
        bytesTransferred = bytesTransferred
    )
}

private fun BlkioStatsConfig.asBlockIOUsage(): BlockIOUsage {
    var bytesWritten: Long = 0
    var bytesRead: Long = 0

    ioServiceBytesRecursive.orEmpty().forEach {
        when (it.op.lowercase()) {
            "write" -> bytesWritten += it.value
            "read" -> bytesRead += it.value
        }
    }

    return BlockIOUsage(
        bytesWritten = bytesWritten,
        bytesRead = bytesRead
    )
}

private fun MemoryStatsConfig.asMemoryUsage(): MemoryUsage {
    val memoryUsageBytes = usage.orDefault() - stats?.cache.orDefault()
    val limitBytes = limit.orDefault()
    val usagePercentage = (memoryUsageBytes.toDouble() / limitBytes.toDouble()) * 100.0
    return MemoryUsage(
        usageBytes = usage.orDefault(),
        limitBytes = limitBytes,
        usagePercent = usagePercentage
    )
}

private fun CpuStatsConfig.asCpuUsage(
    platform: Platform,
    preCpuStats: CpuStatsConfig,
    read: String,
    preRead: String,
    numProcs: Long
): CpuUsage {
    return when (platform) {
        Platform.WINDOWS, Platform.WINDOWSCE -> cpuUsageWindows(preCpuStats, read, preRead, numProcs)
        else -> cpuUsageUnix(preCpuStats)
    }
}

fun CpuStatsConfig.cpuUsageWindows(
    preCpuStats: CpuStatsConfig,
    read: String,
    preRead: String,
    numProcs: Long
): CpuUsage {
    val totalCpuUsage = cpuUsage?.totalUsage?.toDouble() ?: 0.0
    val totalPreCpuUsage = preCpuStats.cpuUsage?.totalUsage?.toDouble() ?: 0.0

    val readInstant = Instant.parse(read)
    val preReadInstant = Instant.parse(preRead)
    val durationNanos = Duration.between(preReadInstant, readInstant).abs().toNanos()
    val nanosHundreds = durationNanos / 100
    val hundredsPerProcessor = nanosHundreds * numProcs
    val intervalsUsed = totalCpuUsage - totalPreCpuUsage
    val (cpuPercentPerCore, cpuPercentTotal) = if (hundredsPerProcessor > 0) {
        val percentPerCore = intervalsUsed / hundredsPerProcessor.toDouble()
        val percentTotal = percentPerCore / numProcs
        percentPerCore to percentTotal
    } else 0.0 to 0.0

    return CpuUsage(
        usagePercentPerCore = cpuPercentPerCore,
        usagePercentTotal = cpuPercentTotal,
        throttlingData?.asThrottlingData() ?: ThrottlingData(-1, -1, -1)
    )
}

private fun CpuStatsConfig.cpuUsageUnix(preCpuStats: CpuStatsConfig): CpuUsage {
    val totalCpuUSage = cpuUsage?.totalUsage?.toDouble() ?: 0.0
    val totalPreCpuUsage = preCpuStats.cpuUsage?.totalUsage?.toDouble() ?: 0.0
    val cpuDelta = totalCpuUSage - totalPreCpuUsage
    val systemCpuUsage = systemCpuUsage?.toDouble()
    val preSystemCpuUsage = preCpuStats.systemCpuUsage?.toDouble()
    val systemDelta = systemCpuUsage?.minus(preSystemCpuUsage ?: 0.0) ?: 0.0
    val (cpuPercentPerCore, cpuPercentTotal) = if (cpuDelta > 0.0 && systemDelta > 0.0) {
        val cpuCount = cpuUsage?.percpuUsage?.size?.toDouble() ?: preCpuStats.cpuUsage?.percpuUsage?.size?.toDouble()
        ?: onlineCpus?.toDouble() ?: 1.0
        val percentPerCore = (cpuDelta / systemDelta) * (cpuCount * 100.0)
        val percentTotal = percentPerCore / cpuCount
        percentPerCore to percentTotal
    } else 0.0 to 0.0
    return CpuUsage(
        usagePercentPerCore = cpuPercentPerCore,
        usagePercentTotal = cpuPercentTotal,
        throttlingData?.asThrottlingData() ?: ThrottlingData(-1, -1, -1)
    )
}

private fun ThrottlingDataConfig.asThrottlingData(): ThrottlingData {
    return ThrottlingData(
        periods = periods.orDefault(),
        throttledPeriods = throttledPeriods.orDefault(),
        throttledTime = throttledTime.orDefault()
    )
}

private fun Long?.orDefault(): Long {
    return this ?: -1
}
