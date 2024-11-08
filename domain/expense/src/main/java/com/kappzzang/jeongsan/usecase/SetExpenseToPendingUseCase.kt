package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import javax.inject.Inject

class SetExpenseToPendingUseCase @Inject constructor(private val expenseDetailRepository: ExpenseDetailRepository) {
    suspend operator fun invoke(expenseId: String): Result<Unit> =
        expenseDetailRepository.setExpenseState(expenseId, ExpenseState.TRANSFER_PENDING)
}
