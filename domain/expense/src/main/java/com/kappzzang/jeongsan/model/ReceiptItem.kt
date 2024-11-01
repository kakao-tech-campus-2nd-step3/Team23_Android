package com.kappzzang.jeongsan.model

import java.time.LocalDateTime

data class ReceiptItem(
    val title: String,
    val categoryColor: String,
    val imageBase64: String?,
    val expenseDetailItemList: List<ReceiptDetailItem>,
    val paymentTime: LocalDateTime = LocalDateTime.now()
)
