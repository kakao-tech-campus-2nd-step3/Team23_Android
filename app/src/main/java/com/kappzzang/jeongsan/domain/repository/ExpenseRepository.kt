package com.kappzzang.jeongsan.domain.repository

import com.kappzzang.jeongsan.domain.model.ExpenseItem

interface ExpenseRepository {
    suspend fun getExpense(id: Long): ExpenseItem
}