package com.krillsson.sysapi.persistence

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module
import com.krillsson.sysapi.config.SysAPIConfiguration
import com.krillsson.sysapi.core.history.db.*
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.flyway.FlywayBundle
import io.dropwizard.hibernate.HibernateBundle
import org.hibernate.cfg.Configuration

fun createHibernateBundle() = object : HibernateBundle<SysAPIConfiguration>(
    HistorySystemLoadEntity::class.java,
    BasicHistorySystemLoadEntity::class.java,
    Connectivity::class.java,
    CpuLoad::class.java,
    NetworkInterfaceLoad::class.java,
    DriveLoad::class.java,
    DiskLoad::class.java,
    FileSystemLoad::class.java,
    MemoryLoad::class.java,
    GpuLoad::class.java,
    HealthData::class.java,
    CpuHealth::class.java,
    CoreLoad::class.java,
    LoadAverages::class.java,
    DriveHealthData::class.java
) {

    override fun createHibernate5Module(): Hibernate5Module {
        return super.createHibernate5Module()
    }

    override fun configure(configuration: Configuration?) {
        super.configure(configuration)
    }

    override fun getDataSourceFactory(configuration: SysAPIConfiguration): DataSourceFactory {
        return configuration.database
    }
}

fun createFlywayBundle() = object : FlywayBundle<SysAPIConfiguration>() {
    override fun getDataSourceFactory(configuration: SysAPIConfiguration): DataSourceFactory {
        return configuration.database
    }
}



