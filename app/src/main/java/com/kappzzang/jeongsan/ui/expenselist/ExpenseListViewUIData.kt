package com.kappzzang.jeongsan.ui.expenselist

import com.kappzzang.jeongsan.model.ExpenseItem

data class ExpenseListViewUIData(
    val totalPriceText: String,
    val priceToSendText: String,
    val groupNameText: String,
    val expenseItems: List<com.kappzzang.jeongsan.model.ExpenseItem>
)
