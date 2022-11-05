package com.krillsson.sysapi.util

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

fun ObjectMapper.configure() {
    registerKotlinModule()
    enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
    enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES)
}