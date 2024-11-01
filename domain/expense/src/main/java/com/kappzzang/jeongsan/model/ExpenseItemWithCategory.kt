package com.kappzzang.jeongsan.model

import java.time.LocalDateTime

data class ExpenseItemWithCategory(
    val item: ExpenseItem,
    val categoryColor: String,
    val date: LocalDateTime
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
            date = LocalDateTime.now()
        )
    }
}
