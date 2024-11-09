package com.kappzzang.jeongsan.util

import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateConverter {

    // "2024-11-09T08:05:08.910678" => "2024-11-09 08:05:08.910678"
    private fun fixTimestampFormat(timestamp: String) = timestamp
        .replace('T', ' ')
        .replace('Z', ' ')

    fun parseFromString(timeStamp: String): LocalDateTime {
        // String을 Date로 변환
        val fixedTimeStamp = fixTimestampFormat(timeStamp)
        val date = Timestamp.valueOf(fixedTimeStamp)

        // Date를 서울 타임존의 Instant로 변환
        val instant = Instant.ofEpochMilli(date.time)
            .atZone(ZoneId.systemDefault())

        // Instant를 LocalDateTime으로 변환
        return instant.toLocalDateTime()
    }

    fun LocalDateTime.formatToTransferString(): String =
        this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREAN))

    fun LocalDateTime.formatToExpenseDate(): String =
        this.format(DateTimeFormatter.ofPattern("MM/dd HH:mm", Locale.KOREAN))
}
