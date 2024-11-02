package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.ReceiptItem
import com.kappzzang.jeongsan.repository.ExpenseRepository
import javax.inject.Inject

class UploadExpenseUseCase @Inject constructor(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(receiptItem: ReceiptItem, groupId: String): String =
        expenseRepository.uploadExpense(receiptItem, groupId)
}
