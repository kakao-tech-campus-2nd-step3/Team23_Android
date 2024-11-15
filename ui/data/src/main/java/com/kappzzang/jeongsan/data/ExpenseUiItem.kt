package com.kappzzang.jeongsan.data

import java.time.LocalDateTime

data class ExpenseUiItem(
    val id: String,
    val name: String,
    val isFirstItem: Boolean,
    val isLastItem: Boolean,
    val price: String,
    val date: LocalDateTime,
    val categoryColor: String,
    val isMyPayment: Boolean,
    val myPrice: String,
    val indicateMyPrice: Boolean = false,
    val indicateNotConfirmedDot: Boolean = false
)
