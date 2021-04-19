package com.krillsson.sysapi.graphql.mutations

import java.util.*

data class DeleteEventOutput(val removed: Boolean)

data class DeleteMonitorOutput(val removed: Boolean)

data class CreateMonitorOutput(val monitorId: UUID)