package com.kappzzang.jeongsan.domain.repository

import com.kappzzang.jeongsan.domain.model.ExpenseDetailItem

interface ExpenseDetailRepository {
    suspend fun getExpenseDetail(): List<ExpenseDetailItem>
    suspend fun saveExpenseDetail(edited: List<ExpenseDetailItem>)
}
