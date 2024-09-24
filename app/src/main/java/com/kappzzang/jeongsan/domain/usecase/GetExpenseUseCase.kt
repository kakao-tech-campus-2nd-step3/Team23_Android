package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.repository.ExpenseRepository

class GetExpenseUseCase (
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(expenseId: Long) =
        expenseRepository.getExpense(expenseId)
}