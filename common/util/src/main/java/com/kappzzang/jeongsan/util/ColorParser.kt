package com.kappzzang.jeongsan.util

import java.util.regex.Pattern

object ColorParser {
    private val HEXADECIMAL_PATTERN: Pattern = Pattern.compile("\\p{XDigit}+")

    private fun isHexadecimal(input: String): Boolean {
        val matcher = HEXADECIMAL_PATTERN.matcher(input)
        return matcher.matches()
    }

    fun parseColor(color: String): String = if (color.startsWith('#')) {
        color
    } else if (isHexadecimal(color) && (color.length == 6 || color.length == 8)) {
        "#$color"
    } else {
        throw IllegalArgumentException("The Input is not a hexcolor")
    }
}
