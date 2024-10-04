package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.model.OcrResultResponse

interface ReceiptCaptureRepository {
    suspend fun getAnalyzedReceiptImage(
        encodedReceiptImage: String
    ): OcrResultResponse
}
