package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.repository.ExpenseRepository
import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject

class ForceFetchExpenseListUseCase @Inject constructor(
    private val repository: ExpenseRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(
        groupId: String,
        queryExpenseState: ExpenseState
    ): Result<ExpenseListResponse> = repository.forceGetExpenseList(
        groupId,
        queryExpenseState,
        authenticationRepository.getServiceId()
    )
}
