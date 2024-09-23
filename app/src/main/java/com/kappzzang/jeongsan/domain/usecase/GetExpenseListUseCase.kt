package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.model.ExpenseListResponse
import com.kappzzang.jeongsan.domain.model.ExpenseState
import com.kappzzang.jeongsan.domain.repository.ExpenseListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpenseListUseCase @Inject constructor(private val repository: ExpenseListRepository) {
    operator fun invoke(
        groupId: String,
        queryExpenseState: ExpenseState
    ) : Flow<ExpenseListResponse> = repository.getExpenseList(groupId, queryExpenseState)
}
