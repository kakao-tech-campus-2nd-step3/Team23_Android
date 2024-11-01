package com.kappzzang.jeongsan.model

enum class ExpenseState { CONFIRMED, NOT_CONFIRMED, TRANSFER_PENDING, TRANSFERED }

data class ExpenseItem(val id: String, val name: String, val price: Int, val state: ExpenseState) {
    companion object {
        val EMPTY = ExpenseItem(
            id = "",
            name = "",
            price = 0,
            state = ExpenseState.NOT_CONFIRMED
        )
    }
}
