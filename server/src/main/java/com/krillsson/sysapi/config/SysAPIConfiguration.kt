/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */
package com.krillsson.sysapi.config

import com.krillsson.sysapi.util.FileSystem
import com.smoketurner.dropwizard.graphql.GraphQLFactory
import io.dropwizard.core.Configuration
import io.dropwizard.core.server.DefaultServerFactory
import io.dropwizard.db.DataSourceFactory

class SysAPIConfiguration(
    val user: UserConfiguration,
    val metricsConfig: MetricsConfiguration,
    val windows: WindowsConfiguration,
    val connectivityCheck: ConnectivityCheckConfiguration,
    val graphQLPlayGround: GraphQLPlayGroundConfiguration,
    val graphql: GraphQLFactory,
    val docker: DockerConfiguration,
    val forwardHttpToHttps: Boolean,
    val selfSignedCertificates: SelfSignedCertificateConfiguration,
    val mDNS: MdnsConfiguration = MdnsConfiguration(false),
    val database: DataSourceFactory = DataSourceFactory()
        .apply {
            driverClass = "org.sqlite.JDBC"
            url = "jdbc:sqlite:${FileSystem.data.absolutePath}/database.sqlite"
            properties = mapOf(
                "charSet" to "UTF-8",
                "hibernate.dialect" to "org.hibernate.dialect.SQLiteDialect"
                /**
                for troubleshooting SQL
                "hibernate.show_sql" to "true",
                "hibernate.use_sql_comments" to "true",
                "hibernate.format_sql" to "true",
                "hibernate.generate_statistics" to "true"
                 **/
            )
        }
) : Configuration() {
    init {
        //disable admin interface
        (serverFactory as DefaultServerFactory).adminConnectors = emptyList()
    }
}