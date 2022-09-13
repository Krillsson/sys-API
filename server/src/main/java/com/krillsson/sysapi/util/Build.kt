package com.krillsson.sysapi.util

import java.util.*

object Build {
    private const val FILENAME = "build.properties"
    private val properties = Properties()

    init {
        val file = this::class.java.classLoader.getResourceAsStream(FILENAME)
        properties.load(file)
    }

    val version: String
        get() = getProperty("version")
    val date: String
        get() = getProperty("date")

    private fun getProperty(key: String): String = properties.getProperty(key)
}