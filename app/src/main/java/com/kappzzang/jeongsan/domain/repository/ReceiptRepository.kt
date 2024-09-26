package com.kappzzang.jeongsan.domain.repository

import com.kappzzang.jeongsan.domain.model.ReceiptItem

interface ReceiptRepository {
    // 업로드 요청 후, 지출 id를 받아옴
    suspend fun uploadExpense(receiptItem: ReceiptItem): String
}