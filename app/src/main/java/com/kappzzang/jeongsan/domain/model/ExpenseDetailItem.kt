package com.kappzzang.jeongsan.domain.model

data class ExpenseDetailItem(
    val expenseId: String,
    val itemName: String,
    val itemPrice: Int,
    val itemQuantity: Int,
    val selectedQuantity: Int
)
