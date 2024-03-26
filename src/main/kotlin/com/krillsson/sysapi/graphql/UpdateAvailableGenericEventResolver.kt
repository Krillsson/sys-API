package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.core.genericevents.GenericEvent
import com.krillsson.sysapi.util.toOffsetDateTime
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component

@Component
class UpdateAvailableGenericEventResolver : GraphQLResolver<GenericEvent.UpdateAvailable> {
    fun getTitle(event: GenericEvent.UpdateAvailable): String {
        return "sys-API update available"
    }

    fun getDescription(event: GenericEvent.UpdateAvailable): String {
        return "New version ${event.newVersion} published at ${event.publishDate}. Server is running ${event.currentVersion}"
    }

    fun dateTime(event: GenericEvent.UpdateAvailable) = event.timestamp.toOffsetDateTime()
}