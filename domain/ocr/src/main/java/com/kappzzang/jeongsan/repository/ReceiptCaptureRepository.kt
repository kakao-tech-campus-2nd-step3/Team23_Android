package com.kappzzang.jeongsan.repository

interface ReceiptCaptureRepository {
    suspend fun getAnalyzedReceiptImage(encodedReceiptImage: String): com.kappzzang.jeongsan.model.OcrResultResponse
}
