package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import javax.inject.Inject

class RevertExpenseToOngoingUseCase @Inject constructor(
    private val expenseDetailRepository: ExpenseDetailRepository
) {
    suspend operator fun invoke(expenseId: String, groupId: String): Result<Unit> =
        expenseDetailRepository.updateExpenseStateToOngoing(
            expenseId = expenseId,
            groupId = groupId)
}
