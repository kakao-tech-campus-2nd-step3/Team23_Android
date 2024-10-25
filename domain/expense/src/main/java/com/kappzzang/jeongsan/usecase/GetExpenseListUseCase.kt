package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.repository.ExpenseRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetExpenseListUseCase @Inject constructor(private val repository: ExpenseRepository) {
    operator fun invoke(
        groupId: String,
        queryExpenseState: ExpenseState
    ): Flow<ExpenseListResponse> = repository.getExpenseList(
        groupId,
        queryExpenseState
    )
}
