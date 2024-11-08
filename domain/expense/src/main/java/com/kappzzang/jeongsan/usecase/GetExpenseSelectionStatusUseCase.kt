package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.ExpenseSelectionStatus
import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import javax.inject.Inject

class GetExpenseSelectionStatusUseCase @Inject constructor(
    private val repository: ExpenseDetailRepository
) {
    suspend operator fun invoke(expenseId: String): Result<ExpenseSelectionStatus> =
        repository.getExpenseSelectionStatus(
            expenseId
        )
}
