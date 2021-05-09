package com.krillsson.sysapi.docker

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
        exposedPorts.map { it.asPortConfig() }
    )
}

fun ExposedPort.asPortConfig() : PortConfig {
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

fun com.github.dockerjava.api.model.Container.asContainer(config: Config): Container {
    return Container(
        command,
        created,
        hostConfig?.asHostConfig() ?: HostConfig("N/A"),
        config,
        id,
        image,
        imageId.orNotApplicable(),
        labels,
        mounts.asMounts(),
        names.toList(),
        networkSettings?.asNetworkSettings() ?: emptyList(),
        ports.asPorts(),
        sizeRootFs ?: 0L,
        sizeRw ?: 0L,
        State.fromString(state),
        status
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
    return networks.map {
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