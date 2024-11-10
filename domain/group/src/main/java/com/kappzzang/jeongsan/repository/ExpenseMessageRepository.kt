package com.kappzzang.jeongsan.repository

interface ExpenseMessageRepository {

    suspend fun sendNewExpenseMessage(
        expenseId: String,
        expenseName: String,
        payerName: String,
        expenseTime: String,
        groupId: String,
        memberUuidList: List<String>
    ): Boolean
}
