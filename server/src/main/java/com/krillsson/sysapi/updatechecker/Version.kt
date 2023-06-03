package com.krillsson.sysapi.updatechecker

class Version(version: String) : Comparable<Version> {
    val numbers: IntArray

    override fun toString(): String {
        return numbers.joinToString(".")
    }

    init {
        val split =
            version.split("\\-".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()[0].split("\\.".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
        numbers = IntArray(split.size)
        for (i in split.indices) {
            numbers[i] = Integer.valueOf(split[i])
        }
    }

    override fun compareTo(another: Version): Int {
        val maxLength = numbers.size.coerceAtLeast(another.numbers.size)
        for (i in 0 until maxLength) {
            val left = if (i < numbers.size) numbers[i] else 0
            val right = if (i < another.numbers.size) another.numbers[i] else 0
            if (left != right) {
                return if (left < right) -1 else 1
            }
        }
        return 0
    }
}