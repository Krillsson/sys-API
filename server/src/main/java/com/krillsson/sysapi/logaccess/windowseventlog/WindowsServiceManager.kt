package com.krillsson.sysapi.logaccess.windowseventlog

import com.krillsson.sysapi.util.logger
import com.sun.jna.platform.win32.W32ServiceManager
import com.sun.jna.platform.win32.WinNT.SERVICE_TYPE_ALL
import com.sun.jna.platform.win32.Winsvc.*


class WindowsServiceManager {

    private val logger by logger()
    fun services(): List<WindowsService> {
        return try {
            val services = mutableListOf<WindowsService>()
            val manager = W32ServiceManager()
            manager.open(SC_MANAGER_ENUMERATE_SERVICE or SC_MANAGER_CONNECT)
            manager.use { manager ->
                for (essp in manager.enumServicesStatusExProcess(SERVICE_TYPE_ALL, SERVICE_STATE_ALL, null)) {
                    val type = WindowsServiceType.fromIntegerConstant(essp.ServiceStatusProcess.dwServiceType)
                    val state: WindowsServiceState =
                        WindowsServiceState.fromIntegerConstant(essp.ServiceStatusProcess.dwCurrentState)
                    val pid = essp.ServiceStatusProcess.dwProcessId
                    services.add(
                        WindowsService(essp.lpServiceName, essp.lpDisplayName, type, state, pid)
                    )
                }
            }
            services
        } catch (throwable: Throwable) {
            logger.error("Error while querying services: ${throwable.message}", throwable)
            emptyList()
        }
    }

    fun service(name: String): WindowsService? {
        return services().firstOrNull { it.name == name }
    }

    fun performWindowsServiceCommand(serviceName: String, command: WindowsServiceCommand): WindowsServiceCommandResult {
        return try {
            val manager = W32ServiceManager()
            manager.open(SC_MANAGER_ENUMERATE_SERVICE or SC_MANAGER_CONNECT)
            manager.openService(serviceName, SERVICE_ALL_ACCESS).use { service ->
                when (command) {
                    WindowsServiceCommand.START -> service.startService()
                    WindowsServiceCommand.STOP -> service.stopService()
                    WindowsServiceCommand.PAUSE -> service.pauseService()
                    WindowsServiceCommand.CONTINUE -> service.continueService()
                }
                WindowsServiceCommandResult.Success
            }
        } catch (throwable: Throwable) {
            logger.error("${command.name} on $serviceName failed with: ${throwable.message}", throwable)
            WindowsServiceCommandResult.Failed(throwable)
        }
    }
}
