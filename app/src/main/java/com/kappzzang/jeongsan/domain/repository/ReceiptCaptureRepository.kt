package com.kappzzang.jeongsan.domain.repository

import com.kappzzang.jeongsan.domain.model.OcrResultResponse
import java.io.File

interface ReceiptCaptureRepository {
    suspend fun getOcrImage(encodedReceiptImage: String): OcrResultResponse
}
