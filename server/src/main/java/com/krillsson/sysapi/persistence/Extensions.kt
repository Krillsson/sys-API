package com.krillsson.sysapi.persistence

import com.krillsson.sysapi.config.SysAPIConfiguration
import com.krillsson.sysapi.core.history.db.*
import com.krillsson.sysapi.util.FileSystem
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.flyway.FlywayBundle
import io.dropwizard.flyway.FlywayFactory
import io.dropwizard.hibernate.HibernateBundle

fun createHibernateBundle() = object : HibernateBundle<SysAPIConfiguration>(
    HistorySystemLoadEntity::class.java,
    Connectivity::class.java,
    CpuLoad::class.java,
    NetworkInterfaceLoad::class.java,
    DriveLoad::class.java,
    MemoryLoad::class.java,
    GpuLoad::class.java,
    HealthData::class.java,
    CpuHealth::class.java,
    CoreLoad::class.java,
    DriveHealthData::class.java
) {
    override fun getDataSourceFactory(configuration: SysAPIConfiguration): DataSourceFactory {
        val dataSourceFactory = configuration.dataSourceFactory
        dataSourceFactory.url = "jdbc:sqlite:${FileSystem.data.absolutePath}/database.sqlite"
        return dataSourceFactory
    }
}

fun createFlywayBundle() = object : FlywayBundle<SysAPIConfiguration>() {
    override fun getDataSourceFactory(configuration: SysAPIConfiguration): DataSourceFactory {
        return configuration.dataSourceFactory
    }

    override fun getFlywayFactory(configuration: SysAPIConfiguration?): FlywayFactory {
        return super.getFlywayFactory(configuration)
    }
}



