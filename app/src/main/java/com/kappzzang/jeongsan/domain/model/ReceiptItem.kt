package com.kappzzang.jeongsan.domain.model

import android.media.Image

data class ReceiptItem(
    val title: String,
    val image: Image,
    val expenseDetailItemList: List<ExpenseDetailItem>
)
