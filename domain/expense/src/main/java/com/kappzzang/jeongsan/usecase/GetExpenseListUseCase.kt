package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.repository.ExpenseListRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetExpenseListUseCase @Inject constructor(private val repository: ExpenseListRepository) {
    operator fun invoke(
        groupId: String,
        queryExpenseState: com.kappzzang.jeongsan.model.ExpenseState
    ): Flow<com.kappzzang.jeongsan.model.ExpenseListResponse> = repository.getExpenseList(groupId, queryExpenseState)
}
