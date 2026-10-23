package com.krillsson.sysapi

import com.krillsson.sysapi.config.CacheConfiguration
import com.krillsson.sysapi.config.MetricsConfiguration
import com.krillsson.sysapi.config.YAMLConfigFile
import com.krillsson.sysapi.core.domain.system.Platform
import com.krillsson.sysapi.core.metrics.Metrics
import com.krillsson.sysapi.core.metrics.defaultimpl.DefaultMetrics
import com.krillsson.sysapi.core.metrics.linux.LinuxMetrics
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.beans.factory.ObjectProvider

class SysApiConfigurationTest {
    private val defaultMetrics = mockk<DefaultMetrics>(relaxed = true)
    private val linuxMetrics = mockk<LinuxMetrics>(relaxed = true)
    private val linuxMetricsProvider = mockk<ObjectProvider<LinuxMetrics>> {
        every { getObject() } returns linuxMetrics
    }
    private val configuration = mockk<YAMLConfigFile> {
        every { metricsConfig } returns MetricsConfiguration(cache = CacheConfiguration(enabled = false))
    }

    @ParameterizedTest
    @EnumSource(Platform::class, names = ["LINUX"], mode = EnumSource.Mode.EXCLUDE)
    fun `uses the OSHI metrics on every platform but Linux`(platform: Platform) {
        // When
        val metrics = metricsFor(platform)

        // Then
        metrics shouldBe defaultMetrics
        verify { defaultMetrics.initialize() }
        verify(exactly = 0) { linuxMetricsProvider.getObject() }
    }

    @Test
    fun `uses the Linux metrics on Linux`() {
        // When
        val metrics = metricsFor(Platform.LINUX)

        // Then
        metrics shouldBe linuxMetrics
        verify { linuxMetrics.initialize() }
    }

    private fun metricsFor(platform: Platform): Metrics {
        return SysApiConfiguration().metrics(
            configuration,
            linuxMetricsProvider,
            defaultMetrics,
            mockk(),
            mockk(),
            platform,
            mockk(),
            mockk(),
            mockk()
        )
    }
}
