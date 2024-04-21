package com.krillsson.sysapi.core.webservicecheck

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WebServerCheckRepository : JpaRepository<WebServerCheckEntity, UUID>