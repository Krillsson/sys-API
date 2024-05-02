package com.krillsson.sysapi.graphql.mutations

import java.util.*

interface AddWebServerCheckOutput

data class AddWebServerCheckOutputSuccess(
        val id: UUID
) : AddWebServerCheckOutput

data class AddWebServerCheckOutputFailed(
        val reason: String
) : AddWebServerCheckOutput