package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.memory.MemoryLoad
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class MemoryLoadResolver : GraphQLResolver<MemoryLoad> {

}