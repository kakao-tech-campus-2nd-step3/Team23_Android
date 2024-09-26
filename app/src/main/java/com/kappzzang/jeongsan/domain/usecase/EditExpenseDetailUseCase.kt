package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.model.ExpenseDetailItem
import com.kappzzang.jeongsan.domain.repository.ExpenseDetailRepository

class EditExpenseDetailUseCase(private val expenseDetailRepository: ExpenseDetailRepository) {
    suspend operator fun invoke(edited: List<ExpenseDetailItem>) {
        expenseDetailRepository.saveExpenseDetail(edited)
    }
}
