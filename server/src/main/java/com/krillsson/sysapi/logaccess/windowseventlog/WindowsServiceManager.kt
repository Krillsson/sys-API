package com.krillsson.sysapi.logaccess.windowseventlog

import com.sun.jna.platform.win32.W32ServiceManager
import com.sun.jna.platform.win32.WinNT.SERVICE_TYPE_ALL
import com.sun.jna.platform.win32.Winsvc.*


class WindowsServiceManager {
    fun services(): List<WindowsService> {
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
        return services
    }

    fun performWindowsServiceCommand(serviceName: String, command: WindowsServiceCommand): WindowsServiceCommandResult {
        val w32ServiceManager = W32ServiceManager()
        return w32ServiceManager.openService(serviceName, SERVICE_ALL_ACCESS).use { service ->
            when (command) {
                WindowsServiceCommand.START -> service.startService()
                WindowsServiceCommand.STOP -> service.stopService()
                WindowsServiceCommand.PAUSE -> service.pauseService()
                WindowsServiceCommand.CONTINUE -> service.continueService()
            }
            WindowsServiceCommandResult.Success
        }
    }
}
