package com.kappzzang.jeongsan.usecase

import javax.inject.Inject

class UploadExpenseUseCase @Inject constructor(private val receiptRepository: com.kappzzang.jeongsan.repository.ReceiptRepository) {
    suspend operator fun invoke(receiptItem: com.kappzzang.jeongsan.model.ReceiptItem): String =
        receiptRepository.uploadExpense(receiptItem)
}
