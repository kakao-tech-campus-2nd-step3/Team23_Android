package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.repository.ExpenseRepository

class GetExpenseUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(expenseId: Long) = expenseRepository.getExpense(expenseId)
}
