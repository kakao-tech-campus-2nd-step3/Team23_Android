package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.repository.ExpenseRepository
import javax.inject.Inject

class ForceFetchExpenseListUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        groupId: String,
        queryExpenseState: ExpenseState
    ): Result<ExpenseListResponse> = repository.forceGetExpenseList(
        groupId,
        queryExpenseState
    )
}
