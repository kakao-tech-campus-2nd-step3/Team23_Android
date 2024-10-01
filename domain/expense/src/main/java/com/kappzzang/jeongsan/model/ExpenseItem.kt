package com.kappzzang.jeongsan.model

import java.util.Date

enum class ExpenseState { CONFIRMED, NOT_CONFIRMED, TRANSFER_PENDING, TRANSFERED }

data class ExpenseItem(
    val id: String,
    val name: String,
    val payer: MemberItem,
    val price: Int,
    val expenseImageUrl: String,
    val date: Date,
    val state: ExpenseState,
    val categoryColor: String
) {
    companion object {
        val EMPTY = ExpenseItem(
            "",
            "",
            MemberItem(
                "",
                "",
                "",
                false
            ),
            0,
            "",
            Date(),
            ExpenseState.NOT_CONFIRMED,
            ""
        )
    }
}