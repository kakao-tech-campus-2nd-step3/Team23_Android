package com.kappzzang.jeongsan.model

import java.time.LocalDateTime

enum class ExpenseState { CONFIRMED, NOT_CONFIRMED, TRANSFER_PENDING, TRANSFERED }

data class ExpenseItem(
    val id: String,
    val name: String,
    val payerName: String,
    val payerMemberId: String,
    val price: Int,
    val expenseImageUrl: String,
    val date: LocalDateTime,
    val state: ExpenseState,
    val categoryColor: String
) {
    companion object {
        val EMPTY = ExpenseItem(
            id = "",
            name = "",
            payerName = "",
            payerMemberId = "",
            price = 0,
            expenseImageUrl = "",
            date = LocalDateTime.now(),
            state = ExpenseState.NOT_CONFIRMED,
            categoryColor = ""
        )
    }
}
