package com.krillsson.sysapi.core.domain.monitor

import kotlin.math.roundToLong

sealed class MonitoredValue {
    data class NumericalValue(
        val value: Long
    ) : MonitoredValue() {
        operator fun compareTo(other: NumericalValue) = this.value.compareTo(other.value)
    }

    data class FractionalValue(
        val value: Float
    ) : MonitoredValue() {
        operator fun compareTo(other: FractionalValue) = this.value.compareTo(other.value)
    }

    data class BooleanValue(
        val value: Boolean

    ) : MonitoredValue()

    fun toDouble() : Double {
        return when (this) {
            is BooleanValue -> if (value) 1.0 else 0.0
            is FractionalValue -> value.toDouble()
            is NumericalValue -> value.toDouble()
        }
    }
}

fun Long.toNumericalValue() = MonitoredValue.NumericalValue(this)
fun Boolean.toBooleanValue() = MonitoredValue.BooleanValue(this)
fun Float.toFractionalValue() = MonitoredValue.FractionalValue(this)
fun Double.toFractionalValue() = MonitoredValue.FractionalValue(this.toFloat())
fun Double.toBooleanValue() = if (this == 1.0) true.toBooleanValue() else false.toBooleanValue()
fun Double.toNumericalValue() = this.roundToLong().toNumericalValue()
