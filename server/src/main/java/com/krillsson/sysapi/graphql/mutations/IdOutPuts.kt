package com.krillsson.sysapi.graphql.mutations

import java.util.*

data class DeleteEventOutput(val id: UUID)

data class DeleteMonitorOutput(val id: UUID)

data class CreateMonitorOutput(val id: UUID)