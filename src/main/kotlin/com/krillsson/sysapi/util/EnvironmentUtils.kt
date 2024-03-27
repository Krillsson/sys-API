package com.krillsson.sysapi.util

import java.net.InetAddress
import java.net.UnknownHostException

object EnvironmentUtils {

    val hostName: String
        get() = try {
            InetAddress.getLocalHost().hostName
        } catch (e: UnknownHostException) {
            ""
        }
}