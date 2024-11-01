package com.kappzzang.jeongsan.model

data class ExpenseItemWithDetails(
    private val item: ExpenseItem,
    val expenseImageUrl: String,
    val expenseDetails: List<ExpenseDetailItem>

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
        val EMPTY = ExpenseItemWithDetails(
            ExpenseItem.EMPTY,
            "",
            emptyList()
        )
    }
}
