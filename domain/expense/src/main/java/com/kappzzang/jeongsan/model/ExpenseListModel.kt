package com.kappzzang.jeongsan.model

import java.time.LocalDateTime

enum class ExpenseState { CONFIRMED, NOT_CONFIRMED, TRANSFER_PENDING, TRANSFERED }

data class ExpenseListResponse(
    val totalPrice: Int,
    val totalExpenseToSend: Int,
    val expenseList: List<ExpenseItemWithCategory>
) {
    companion object {
        fun emptyList() = ExpenseListResponse(0, 0, listOf())
    }
}

data class ExpenseItemWithCategory(
    private val item: ExpenseItem,
    val categoryColor: String,
    val date: LocalDateTime,
    val isMyPayment: Boolean,
    val personalExpense: Int? = null
) {
    val id
        get() = item.id
    val name
        get() = item.name
    val price
        get() = item.price
    val state
        get() = item.state

    companion object {
        val EMPTY = ExpenseItemWithCategory(
            item = ExpenseItem.EMPTY,
            categoryColor = "",
            date = LocalDateTime.now(),
            isMyPayment = false
        )
    }
}

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
