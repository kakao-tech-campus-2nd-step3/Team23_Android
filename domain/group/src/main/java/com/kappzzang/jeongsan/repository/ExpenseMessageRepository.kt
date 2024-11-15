package com.kappzzang.jeongsan.repository

interface ExpenseMessageRepository {

    suspend fun sendNewExpenseMessage(
        expenseId: String,
        expenseName: String,
        payerName: String,
        groupId: String,
        memberUuidList: List<String>
    ): Boolean
}
