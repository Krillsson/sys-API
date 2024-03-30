package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.docker.Container
import com.krillsson.sysapi.docker.ContainerManager
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class ContainerResolver(val containerManager: ContainerManager) {

    @SchemaMapping(typeName="DockerContainerMetricsHistoryEntry", field="metrics")
    fun metrics(container: Container) = containerManager.statsForContainer(container.id)
}