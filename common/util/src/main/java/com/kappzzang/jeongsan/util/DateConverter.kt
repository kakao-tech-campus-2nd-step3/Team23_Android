package com.kappzzang.jeongsan.util

import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateConverter {
    fun parseFromString(timeStamp: String): LocalDateTime {

        // String을 Date로 변환
        val date = Timestamp.valueOf(timeStamp)

        // Date를 서울 타임존의 Instant로 변환
        val instant = Instant.ofEpochMilli(date.time)
            .atZone(ZoneId.of("Asia/Seoul"))

        // Instant를 LocalDateTime으로 변환
        return instant.toLocalDateTime()
    }

    fun LocalDateTime.formatToExpenseDate(): String =
        this.format(DateTimeFormatter.ofPattern("MM/dd HH:mm", Locale.KOREAN))
}
