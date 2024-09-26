package com.kappzzang.jeongsan.domain.model

data class ExpenseListResponse(
    val totalPrice: Int,
    val totalExpenseToSend: Int,
    val expenseList: List<ExpenseItem>
) {
    companion object {
        fun emptyList() = ExpenseListResponse(0, 0, listOf())
    }
}