package com.kappzzang.jeongsan.model

import java.time.LocalDateTime

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
