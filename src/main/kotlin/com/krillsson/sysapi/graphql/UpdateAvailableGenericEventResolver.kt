package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.genericevents.GenericEvent
import com.krillsson.sysapi.util.toOffsetDateTime
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class UpdateAvailableGenericEventResolver {
    @SchemaMapping
    fun title(event: GenericEvent.UpdateAvailable): String {
        return "sys-API update available"
    }

    @SchemaMapping
    fun description(event: GenericEvent.UpdateAvailable): String {
        return "New version ${event.newVersion} published at ${event.publishDate}. Server is running ${event.currentVersion}"
    }

    @SchemaMapping
    fun dateTime(event: GenericEvent.UpdateAvailable) = event.timestamp.toOffsetDateTime()
}