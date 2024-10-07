package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.repository.ExpenseDetailRepository

class GetExpenseDetailUseCase(private val expenseDetailRepository: ExpenseDetailRepository) {
    suspend operator fun invoke(): List<ExpenseDetailItem> =
        expenseDetailRepository.getExpenseDetail()
}
