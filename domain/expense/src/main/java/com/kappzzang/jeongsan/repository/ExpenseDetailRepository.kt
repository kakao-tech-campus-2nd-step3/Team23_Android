package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItemWithDetails

interface ExpenseDetailRepository {
    suspend fun getExpenseDetail(expenseId: String): Result<ExpenseItemWithDetails>
    suspend fun saveExpenseDetail(edited: List<ExpenseDetailItem>, expenseId: String, groupId: String): Result<Unit>
}
