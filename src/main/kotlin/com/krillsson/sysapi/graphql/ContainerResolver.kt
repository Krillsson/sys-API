package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.docker.Container
import com.krillsson.sysapi.docker.ContainerManager
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class ContainerResolver(val containerManager: ContainerManager) : GraphQLResolver<Container> {
    fun metrics(container: Container) = containerManager.statsForContainer(container.id)
}