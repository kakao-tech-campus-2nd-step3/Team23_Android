package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.repository.ExpenseDetailRepository

class EditExpenseDetailUseCase(private val expenseDetailRepository: ExpenseDetailRepository) {
    suspend operator fun invoke(edited: List<ExpenseDetailItem>) {
        expenseDetailRepository.saveExpenseDetail(edited)
    }
}
