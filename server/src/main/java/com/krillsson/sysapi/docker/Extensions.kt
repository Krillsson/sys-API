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

fun Statistics.asStatistics(): ContainerStatistics {
    return ContainerStatistics(
        currentPid = pidsStats.current.orDefault(),
        cpuUsage = cpuStats.asCpuUsage(),
        memoryUsage = memoryStats.asMemoryUsage(),
        networkUsage = networks?.map { it.value.asNetworkUsage(it.key) }.orEmpty(),
        blockIOUsage = blkioStats.asBlockIOUsage()
    )
}

private fun BlkioStatsConfig.asBlockIOUsage(): BlockIOUsage {
    return BlockIOUsage(
        ioMergedRecursive = ioMergedRecursive?.map { it.asBlockIOEntry() }.orEmpty(),
        ioServiceBytesRecursive = ioServiceBytesRecursive?.map { it.asBlockIOEntry() }.orEmpty(),
        ioServicedRecursive = ioServicedRecursive?.map { it.asBlockIOEntry() }.orEmpty(),
        ioQueueRecursive = ioQueueRecursive?.map { it.asBlockIOEntry() }.orEmpty(),
        ioServiceTimeRecursive = ioServiceTimeRecursive?.map { it.asBlockIOEntry() }.orEmpty(),
        ioWaitTimeRecursive = ioWaitTimeRecursive?.map { it.asBlockIOEntry() }.orEmpty(),
        ioTimeRecursive = ioTimeRecursive?.map { it.asBlockIOEntry() }.orEmpty(),
        sectorsRecursive = sectorsRecursive?.map { it.asBlockIOEntry() }.orEmpty(),
    )
}

private fun BlkioStatEntry.asBlockIOEntry(): BlockIOEntry {
    return BlockIOEntry(
        major,
        minor,
        op,
        value
    )
}

private fun StatisticNetworksConfig.asNetworkUsage(name: String): NetworkUsage {
    return NetworkUsage(
        name = name,
        rxBytes = rxBytes.orDefault(),
        rxDropped = rxDropped.orDefault(),
        rxErrors = rxErrors.orDefault(),
        rxPackets = rxPackets.orDefault(),
        txBytes = txBytes.orDefault(),
        txDropped = txDropped.orDefault(),
        txErrors = txErrors.orDefault(),
        txPackets = txPackets.orDefault()
    )
}

private fun MemoryStatsConfig.asMemoryUsage(): MemoryUsage {
    return MemoryUsage(
        usage = usage.orDefault(),
        maxUsage = maxUsage.orDefault(),
        limit = limit.orDefault()
    )
}

private fun CpuStatsConfig.asCpuUsage(): CpuUsage {
    return CpuUsage(
        cpuUsage?.totalUsage.orDefault(),
        cpuUsage?.percpuUsage.orEmpty(),
        cpuUsage?.usageInKernelmode.orDefault(),
        cpuUsage?.usageInUsermode.orDefault(),
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
