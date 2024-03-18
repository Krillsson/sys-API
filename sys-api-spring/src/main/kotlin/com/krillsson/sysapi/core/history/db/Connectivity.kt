package com.krillsson.sysapi.core.history.db

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Entity
class Connectivity(
    @Id
    open var id: UUID,
    open var externalIp: String?,
    open var previousExternalIp: String?,
    open var localIp: String?,
    open var connected: Boolean
)

@Repository
interface ConnectivityDAO : JpaRepository<Connectivity, UUID>