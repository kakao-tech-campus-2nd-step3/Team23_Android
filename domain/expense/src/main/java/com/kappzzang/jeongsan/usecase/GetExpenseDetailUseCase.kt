package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.ExpenseItemWithDetails
import com.kappzzang.jeongsan.repository.ExpenseDetailRepository

class GetExpenseDetailUseCase(private val expenseDetailRepository: ExpenseDetailRepository) {
    suspend operator fun invoke(
        expenseId: String
    ): ExpenseItemWithDetails =
        expenseDetailRepository.getExpenseDetail(expenseId)
}
