package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import javax.inject.Inject

class SetExpensesToCompleteUseCase @Inject constructor(
    private val expenseDetailRepository: ExpenseDetailRepository
) {
    suspend operator fun invoke(expenseIdList: List<String>, groupId: String): Result<Unit> =
        expenseDetailRepository.updateExpensesStateToPending(
            expenseIds = expenseIdList,
            groupId = groupId
        )
}
