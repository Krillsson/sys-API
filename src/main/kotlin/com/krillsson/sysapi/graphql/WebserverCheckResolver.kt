package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.webservicecheck.WebServerCheck
import com.krillsson.sysapi.core.webservicecheck.WebServerCheckService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.time.Instant

@Controller
@SchemaMapping(typeName = "WebserverCheck")
class WebserverCheckResolver(val webServerCheckService: WebServerCheckService) {
    @SchemaMapping
    fun status(check: WebServerCheck) = webServerCheckService.getStatusForWebServer(check.id)

    @SchemaMapping
    fun historyBetweenTimestamps(check: WebServerCheck, @Argument from: Instant, @Argument to: Instant) = webServerCheckService.getHistoryForWebServerBetweenTimestamps(check.id, from, to)
}