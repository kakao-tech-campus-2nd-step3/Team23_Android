package com.kappzzang.jeongsan.domain.usecase

import com.kappzzang.jeongsan.domain.model.ReceiptItem
import com.kappzzang.jeongsan.domain.repository.ReceiptRepository
import javax.inject.Inject

class UploadExpenseUseCase @Inject constructor(private val receiptRepository: ReceiptRepository) {
    suspend operator fun invoke(receiptItem: ReceiptItem): String =
        receiptRepository.uploadExpense(receiptItem)
}
