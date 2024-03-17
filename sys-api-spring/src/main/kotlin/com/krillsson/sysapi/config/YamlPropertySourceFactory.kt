package com.krillsson.sysapi.config

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory
import java.io.IOException

class YamlPropertySourceFactory : PropertySourceFactory {
    override fun createPropertySource(name: String?, encodedResource: EncodedResource): PropertySource<*> {
        var factory = YamlPropertiesFactoryBean()
        factory.setResources(encodedResource.resource)
        var properties = factory.getObject()
        return PropertiesPropertySource(encodedResource.resource.filename!!, properties!!)
    }
}