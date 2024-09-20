package com.kappzzang.jeongsan.domain.model

sealed class ExpenseListResponse {
    data class OnCalculatingList(
        val totalPrice: Int,
        val expenseList: List<ExpenseItem>
    ) : ExpenseListResponse()

    data class PendingSendList(
        val totalPrice: Int,
        val totalExpenseToSend: Int,
        val expenseList: List<ExpenseItem>
    ) : ExpenseListResponse()

    data class SendingCompleteList(
        val totalPrice: Int,
        val expenseList: List<ExpenseItem>
    ) : ExpenseListResponse()
}
