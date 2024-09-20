package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.model.ExpenseListResponse
import com.kappzzang.jeongsan.domain.model.ExpenseState
import com.kappzzang.jeongsan.domain.repository.ExpenseListPageRepository
import kotlinx.coroutines.flow.Flow

class GetExpenseListUseCase(private val repository: ExpenseListPageRepository) {
    operator fun invoke(
        groupId: String,
        queryExpenseState: ExpenseState
    ) : Flow<ExpenseListResponse> = repository.getExpenseList(groupId, queryExpenseState)
}
