package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Entity
class Connectivity(
    @Id
    var id: UUID,
    var externalIp: String?,
    var previousExternalIp: String?,
    var localIp: String?,
    var connected: Boolean
)

@Repository
interface ConnectivityDAO : JpaRepository<Connectivity, UUID>