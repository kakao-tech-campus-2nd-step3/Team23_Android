package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.model.ExpenseDetailItem
import com.kappzzang.jeongsan.domain.repository.ExpenseDetailRepository

class GetExpenseDetailUseCase (private val expenseDetailRepository: ExpenseDetailRepository) {
    suspend operator fun invoke(): List<ExpenseDetailItem> =
        expenseDetailRepository.getExpenseDetail()
}