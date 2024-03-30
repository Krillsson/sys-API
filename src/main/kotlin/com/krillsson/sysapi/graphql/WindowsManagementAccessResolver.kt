package com.krillsson.sysapi.graphql

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
}
