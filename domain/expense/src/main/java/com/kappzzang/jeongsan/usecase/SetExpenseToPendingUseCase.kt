package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import javax.inject.Inject

class SetExpenseToPendingUseCase @Inject constructor(
    private val expenseDetailRepository: ExpenseDetailRepository
) {
    suspend operator fun invoke(expenseId: String, groupId: String): Result<Unit> =
        expenseDetailRepository.updateExpenseStateToPending(
            expenseId = expenseId,
            groupId = groupId
        )
}
