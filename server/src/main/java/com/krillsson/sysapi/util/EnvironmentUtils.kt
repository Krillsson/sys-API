package com.krillsson.sysapi.util

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.health.HealthCheckRegistry
import com.krillsson.server.BuildConfig
import com.krillsson.sysapi.SysAPIApplication
import com.krillsson.sysapi.auth.BasicAuthenticator
import com.krillsson.sysapi.auth.BasicAuthorizer
import com.krillsson.sysapi.config.UserConfiguration
import com.krillsson.sysapi.core.history.LegacyHistoryManager
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.monitoring.MonitorManager
import com.krillsson.sysapi.core.monitoring.event.EventManager
import com.krillsson.sysapi.rest.*
import io.dropwizard.auth.AuthDynamicFeature
import io.dropwizard.auth.AuthValueFactoryProvider
import io.dropwizard.auth.basic.BasicCredentialAuthFilter
import io.dropwizard.core.setup.Environment
import io.dropwizard.jersey.setup.JerseyEnvironment
import io.dropwizard.jetty.setup.ServletEnvironment
import io.dropwizard.lifecycle.Managed
import io.dropwizard.lifecycle.setup.LifecycleEnvironment
import org.eclipse.jetty.servlet.FilterHolder
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlets.CrossOriginFilter
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature
import oshi.software.os.OperatingSystem
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

fun JerseyEnvironment.registerJerseyResources(
    os: OperatingSystem,
    provider: Metrics,
    historyManager: LegacyHistoryManager,
    monitorManager: MonitorManager,
    eventManager: EventManager,
    endpoints: Array<String>
) {
    register(
        SystemResource(
            provider.systemMetrics(),
            historyManager, { os.systemUptime }
        )
    )
    register(DrivesResource(provider.driveMetrics(), historyManager))
    register(DisksResource(provider.diskMetrics(), historyManager))
    register(FileSystemsResource(provider.fileSystemMetrics(), historyManager))
    register(GpuResource(provider.gpuMetrics(), historyManager))
    register(MemoryResource(provider.memoryMetrics(), historyManager))
    register(NetworkInterfacesResource(provider.networkMetrics(), historyManager))
    register(ProcessesResource(provider.processesMetrics()))
    register(CpuResource(provider.cpuMetrics(), historyManager))
    register(MotherboardResource(provider.motherboardMetrics()))
    register(EventResource(eventManager))
    register(MonitorResource(monitorManager, eventManager))
    register(
        MetaInfoResource(
            BuildConfig.APP_VERSION,
            endpoints,
            os.processId
        )
    )
}

fun LifecycleEnvironment.registerManagedObjects(
    vararg managedObjects: Managed
) {
    managedObjects.forEach { item ->
        manage(item)
    }
}

fun JerseyEnvironment.registerFeatures(userConfig: UserConfiguration) {
    val userBasicCredentialAuthFilter = BasicCredentialAuthFilter.Builder<UserConfiguration>()
        .setAuthenticator(BasicAuthenticator(userConfig))
        .setRealm(SysAPIApplication.NAME)
        .setAuthorizer(BasicAuthorizer(userConfig))
        .buildAuthFilter()
    register(RolesAllowedDynamicFeature::class.java)
    register(OffsetDateTimeConverter::class.java)
    register(AuthDynamicFeature(userBasicCredentialAuthFilter))
    register(AuthValueFactoryProvider.Binder(UserConfiguration::class.java))
}

fun ServletEnvironment.configureCrossOriginFilter() {
    val cors: FilterRegistration.Dynamic = addFilter("cors", CrossOriginFilter::class.java)
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType::class.java), true, "/*")
}

fun MetricRegistry.disableMetrics() {
    names.forEach { name ->
        remove(name)
    }
}

fun HealthCheckRegistry.disableHealthChecks() {
    names.forEach { name ->
        unregister(name)
    }
}

object EnvironmentUtils {

    fun addHttpsForward(handler: ServletContextHandler) {
        handler.addFilter(FilterHolder(object : Filter {
            override fun init(filterConfig: FilterConfig) {
            }

            override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
                val uri = (request as HttpServletRequest).requestURL
                if (uri.toString().startsWith("http://")) {
                    val location = "https://" + uri.substring("http://".length)
                    (response as HttpServletResponse).sendRedirect(location)
                } else {
                    chain.doFilter(request, response)
                }
            }

            override fun destroy() {}
        }), "/*", EnumSet.of(DispatcherType.REQUEST))
    }

    fun getEndpoints(environment: Environment): Array<String> {
        val NEWLINE = String.format("%n", *arrayOfNulls<Any>(0))
        val arr = environment.jersey().resourceConfig.endpointsInfo
            .replace("The following paths were found for the configured resources:", "")
            .replace("    ", "")
            .replace(" \\(.+?\\)".toRegex(), "")
            .split(NEWLINE).toTypedArray()
        return Arrays.copyOfRange(arr, 2, arr.size - 1)
    }

    val hostName: String
        get() = try {
            InetAddress.getLocalHost().hostName
        } catch (e: UnknownHostException) {
            ""
        }
}