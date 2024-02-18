package com.krillsson.sysapi.persistence

import com.codahale.metrics.MetricRegistry
import com.krillsson.sysapi.config.SysAPIConfiguration
import com.krillsson.sysapi.util.logger
import io.dropwizard.flyway.FlywayBundle
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.FlywayException

open class PersistenceMigrator(
    private val config: SysAPIConfiguration,
    private val flywayBundle: FlywayBundle<SysAPIConfiguration>,
    private val metrics: MetricRegistry
) {

    val logger by logger()

    fun migrate() {
        getFlyway()?.let { migrateSql(it) }
    }

    private fun migrateSql(flyway: Flyway) {
        val result = flyway.migrate()
        if (result.migrationsExecuted > 0) {
            val initialSchemaVersion = result.initialSchemaVersion ?: "0"
            logger.info("Database migration - migrated $initialSchemaVersion to ${result.targetSchemaVersion}")
        }
    }

    private fun getFlyway(): Flyway? {
        return try {
            flywayBundle.getFlywayFactory(config)
                .build(
                    config.database
                        .build(metrics, "Flyway")
                )
        } catch (e: FlywayException) {
            logger.error("Error while migrating", e)
            null
        }
    }
}