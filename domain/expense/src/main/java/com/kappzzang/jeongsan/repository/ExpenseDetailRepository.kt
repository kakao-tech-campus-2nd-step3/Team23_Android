package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItemWithDetails
import com.kappzzang.jeongsan.model.ExpenseSelectionStatus

interface ExpenseDetailRepository {
    suspend fun getExpenseDetail(expenseId: String): Result<ExpenseItemWithDetails>
    suspend fun saveExpenseDetail(
        edited: List<ExpenseDetailItem>,
        expenseId: String,
        groupId: String
    ): Result<Unit>

    suspend fun getExpenseSelectionStatus(expenseId: String): Result<ExpenseSelectionStatus>

    suspend fun updateExpenseStateToPending(
        expenseId: String,
        groupId: String
    ): Result<Unit>

    suspend fun updateExpenseStateToOngoing(
        expenseId: String,
        groupId: String
    ): Result<Unit>
}
