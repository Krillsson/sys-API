package com.krillsson.sysapi.graphql

import com.krillsson.sysapi.config.SysAPIConfiguration
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.core.Configuration
import io.dropwizard.core.setup.Environment

class GraphQLPlaygroundConfiguredBundle : AssetsBundle("/assets", "/", "index.htm", "graphql-playground") {
    override fun run(configuration: Configuration?, environment: Environment?) {
        if(configuration is SysAPIConfiguration && configuration.graphQLPlayGround.enabled) {
            super.run(configuration, environment)
        }
    }
}