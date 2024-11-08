package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseSelectionStatus
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import com.kappzzang.jeongsan.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpenseSelectionStatusUseCase @Inject constructor(private val repository: ExpenseDetailRepository) {
    suspend operator fun invoke(
        expenseId: String
    ): Result<ExpenseSelectionStatus> = repository.getExpenseSelectionStatus(
        expenseId
    )
}
