package com.kappzzang.jeongsan.expenselist

import com.kappzzang.jeongsan.model.ExpenseItem

data class ExpenseListViewUIData(
    val totalPriceText: String,
    val priceToSendText: String,
    val groupNameText: String,
    val expenseItems: List<model.ExpenseItem>
)
