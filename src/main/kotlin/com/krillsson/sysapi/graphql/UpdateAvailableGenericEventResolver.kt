package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.genericevents.GenericEvent
import com.krillsson.sysapi.core.genericevents.UpdateAvailable
import com.krillsson.sysapi.util.toOffsetDateTime
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
@SchemaMapping(typeName = "UpdateAvailable")
class UpdateAvailableGenericEventResolver {
    @SchemaMapping(typeName = "UpdateAvailable", field = "title")
    fun title(event: UpdateAvailable): String {
        return "sys-API update available"
    }

    @SchemaMapping(typeName = "UpdateAvailable", field = "description")
    fun description(event: UpdateAvailable): String {
        return "New version ${event.newVersion} published at ${event.publishDate}. Server is running ${event.currentVersion}"
    }

    @SchemaMapping(typeName = "UpdateAvailable", field = "dateTime")
    fun dateTime(event: UpdateAvailable) = event.timestamp.toOffsetDateTime()
}