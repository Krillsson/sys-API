package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.graphql.domain.WindowsEventLogRecordConnection
import com.krillsson.sysapi.graphql.domain.WindowsManagementAccessAvailable
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class WindowsManagementAccessResolver {

    @SchemaMapping
    fun services(windowsManagementAccessAvailable: WindowsManagementAccessAvailable) = windowsManagementAccessAvailable.services()

    @SchemaMapping
    fun service(windowsManagementAccessAvailable: WindowsManagementAccessAvailable, @Argument name: String) = windowsManagementAccessAvailable.service(name)

    @SchemaMapping
    fun eventLogs(windowsManagementAccessAvailable: WindowsManagementAccessAvailable) = windowsManagementAccessAvailable.eventLogs()

    @SchemaMapping
    fun openEventLogBySource(windowsManagementAccessAvailable: WindowsManagementAccessAvailable, @Argument name: String) = windowsManagementAccessAvailable.openEventLogBySource(name)

    @SchemaMapping
    fun openApplicationEventLog(windowsManagementAccessAvailable: WindowsManagementAccessAvailable) = windowsManagementAccessAvailable.openApplicationEventLog()

    @SchemaMapping
    fun openSystemEventLog(windowsManagementAccessAvailable: WindowsManagementAccessAvailable) = windowsManagementAccessAvailable.openSystemEventLog()

    @SchemaMapping
    fun openSecurityEventLog(windowsManagementAccessAvailable: WindowsManagementAccessAvailable) = windowsManagementAccessAvailable.openSecurityEventLog()

    @SchemaMapping
    fun openEventLogBySourceConnection(
        windowsManagementAccessAvailable: WindowsManagementAccessAvailable,
        @Argument source: String,
        @Argument after: String?,
        @Argument before: String?,
        @Argument first: Int?,
        @Argument last: Int?
    ): WindowsEventLogRecordConnection {
        return windowsManagementAccessAvailable.openEventLogBySourceConnection(
            source,
            after = after,
            before = before,
            first = first,
            last = last
        )
    }

    @SchemaMapping
    fun openSystemEventLogConnection(
        windowsManagementAccessAvailable: WindowsManagementAccessAvailable,
        @Argument after: String?,
        @Argument before: String?,
        @Argument first: Int?,
        @Argument last: Int?
    ): WindowsEventLogRecordConnection {
        return windowsManagementAccessAvailable.openSystemEventLogConnection(
            after = after,
            before = before,
            first = first,
            last = last
        )
    }

    @SchemaMapping
    fun openSecurityEventLogConnection(
        windowsManagementAccessAvailable: WindowsManagementAccessAvailable,
        @Argument after: String?,
        @Argument before: String?,
        @Argument first: Int?,
        @Argument last: Int?
    ): WindowsEventLogRecordConnection {
        return windowsManagementAccessAvailable.openSecurityEventLogConnection(
            after = after,
            before = before,
            first = first,
            last = last
        )
    }

    @SchemaMapping
    fun openApplicationEventLogConnection(
        windowsManagementAccessAvailable: WindowsManagementAccessAvailable,
        @Argument after: String?,
        @Argument before: String?,
        @Argument first: Int?,
        @Argument last: Int?
    ): WindowsEventLogRecordConnection {
        return windowsManagementAccessAvailable.openApplicationEventLogConnection(
            after = after,
            before = before,
            first = first,
            last = last
        )
    }
}
