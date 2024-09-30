package com.kappzzang.jeongsan.repository

interface ReceiptRepository {
    // 업로드 요청 후, 지출 id를 받아옴
    suspend fun uploadExpense(receiptItem: com.kappzzang.jeongsan.model.ReceiptItem): String
}
