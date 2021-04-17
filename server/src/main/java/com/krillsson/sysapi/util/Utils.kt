package com.krillsson.sysapi.util

inline fun <T> measureTimeMillis(function: () -> T): Pair<Long, T> {

    val startTime = System.currentTimeMillis()
    val result: T = function.invoke()
    val elapsedTime = System.currentTimeMillis() - startTime

    return elapsedTime to result
}