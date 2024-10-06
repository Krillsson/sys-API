package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.domain.processes.Process
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
@SchemaMapping(typeName = "Process")
class ProcessResolver {
    @SchemaMapping
    fun id(process: Process) = process.processID
}