package com.kappzzang.jeongsan.model

import java.time.LocalDateTime

enum class ExpenseState { CONFIRMED, NOT_CONFIRMED, TRANSFER_PENDING, TRANSFERED }

data class ExpenseItem(
    val id: String,
    val name: String,
    val price: Int,
    val state: ExpenseState,
) {
    companion object {
        val EMPTY = ExpenseItem(
            id = "",
            name = "",
            price = 0,
            state = ExpenseState.NOT_CONFIRMED,
        )
    }
}
