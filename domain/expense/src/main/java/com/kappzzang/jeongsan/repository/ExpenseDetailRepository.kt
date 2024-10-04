package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.ExpenseDetailItem

interface ExpenseDetailRepository {
    suspend fun getExpenseDetail(): List<ExpenseDetailItem>
    suspend fun saveExpenseDetail(edited: List<ExpenseDetailItem>)
}
