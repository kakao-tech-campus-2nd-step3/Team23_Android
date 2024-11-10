package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.repository.TransferRepository
import javax.inject.Inject

class GetPurchasedExpenseList @Inject constructor(private val transferRepository: TransferRepository) {
    suspend operator fun invoke(groupId: String): Result<List<ExpenseItem>> =
        transferRepository.getPurchasedExpenseList(groupId)
}
