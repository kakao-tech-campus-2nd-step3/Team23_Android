package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.ReceiptItem
import com.kappzzang.jeongsan.repository.ReceiptRepository
import javax.inject.Inject

class UploadExpenseUseCase @Inject constructor(
    private val receiptRepository: ReceiptRepository
) {
    suspend operator fun invoke(receiptItem: ReceiptItem): String =
        receiptRepository.uploadExpense(receiptItem)
}
