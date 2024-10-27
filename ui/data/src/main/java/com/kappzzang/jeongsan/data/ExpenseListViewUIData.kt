package com.kappzzang.jeongsan.data

data class ExpenseListViewUIData(
    val totalPriceText: String,
    val priceToSendText: String,
    val groupNameText: String,
    val expenseItems: List<ExpenseUiItem>
)
