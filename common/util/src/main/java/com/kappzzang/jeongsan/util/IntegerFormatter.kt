package com.kappzzang.jeongsan.util

object IntegerFormatter {
    fun Int.formatDecimalSeparator(): String = toString()
        .reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()
}
