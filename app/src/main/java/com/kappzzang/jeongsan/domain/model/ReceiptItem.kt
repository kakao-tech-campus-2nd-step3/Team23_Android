package com.kappzzang.jeongsan.domain.model

data class ReceiptItem(
    val title: String,
    val categoryColor: String,
    val imageBase64: String?,
    val expenseDetailItemList: List<ReceiptDetailItem>
)
