package com.krillsson.sysapi.core.history.db

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Connectivity(
    @Id
    val id: UUID,
    val externalIp: String?,
    val previousExternalIp: String?,
    val connected: Boolean
)