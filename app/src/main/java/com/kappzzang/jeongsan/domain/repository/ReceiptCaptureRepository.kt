package com.kappzzang.jeongsan.domain.repository

import com.kappzzang.jeongsan.domain.model.OcrResultResponse

interface ReceiptCaptureRepository {
    suspend fun getAnalyzedReceiptImage(encodedReceiptImage: String): OcrResultResponse
}
