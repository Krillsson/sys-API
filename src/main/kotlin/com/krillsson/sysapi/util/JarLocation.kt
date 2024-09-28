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
package com.krillsson.sysapi.util

import com.krillsson.sysapi.SysAPIApplication
import java.io.File
import java.net.URISyntaxException

object JarLocation {
    val SEPARATOR: String = System.getProperty("file.separator")
    private val executionLocation = executionLocation()
    private val IS_JAR = executionLocation.isFile && executionLocation.name.endsWith(".jar")
    private val IS_EXE_WRAPPER = executionLocation.isFile && executionLocation.name.endsWith(".exe")
    private val JAR_LIB_LOCATION = "${executionLocation.parentFile?.parent}${SEPARATOR}lib$SEPARATOR"
    private val EXE_LIB_LOCATION = "${executionLocation.parent}${SEPARATOR}lib$SEPARATOR"

    val SOURCE_LIB_LOCATION: String =
        "${executionLocation.parentFile?.parentFile?.parentFile?.parentFile?.toString()}${SEPARATOR}src${SEPARATOR}dist${SEPARATOR}lib"
    val LIB_LOCATION: String = when {
        IS_JAR -> JAR_LIB_LOCATION
        IS_EXE_WRAPPER -> EXE_LIB_LOCATION
        else -> SOURCE_LIB_LOCATION
    }

    private fun executionLocation(): File {
        return try {
            File(
                SysAPIApplication::class.java.protectionDomain
                    .codeSource
                    .location
                    .toURI()
                    .path
            )
        } catch (e: URISyntaxException) {
            File(SysAPIApplication::class.java.protectionDomain.codeSource.location.path)
        }
    }
}
