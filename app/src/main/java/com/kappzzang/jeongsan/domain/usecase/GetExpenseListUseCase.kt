package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.model.ExpenseListResponse
import com.kappzzang.jeongsan.domain.model.ExpenseState
import com.kappzzang.jeongsan.domain.repository.ExpenseListRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetExpenseListUseCase @Inject constructor(private val repository: ExpenseListRepository) {
    operator fun invoke(
        groupId: String,
        queryExpenseState: ExpenseState
    ): Flow<ExpenseListResponse> = repository.getExpenseList(groupId, queryExpenseState)
}