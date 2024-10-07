package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.ExpenseItem

interface ExpenseRepository {
    suspend fun getExpense(id: Long): ExpenseItem
}
