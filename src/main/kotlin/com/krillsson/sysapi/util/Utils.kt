package com.krillsson.sysapi.util

import java.math.BigDecimal
import java.math.RoundingMode

inline fun <T> measureTimeMillis(function: () -> T): Pair<Long, T> {

    val startTime = System.currentTimeMillis()
    val result: T = function.invoke()
    val elapsedTime = System.currentTimeMillis() - startTime

    return elapsedTime to result
}

fun Double.round(places: Int): Double {
    if (java.lang.Double.isInfinite(this) || java.lang.Double.isNaN(this)) {
        return 0.0
    }
    require(!(places < 0))
    var bd = BigDecimal.valueOf(this)
    bd = bd.setScale(places, RoundingMode.HALF_UP)
    return bd.toDouble()
}