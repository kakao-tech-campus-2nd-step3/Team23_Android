package com.kappzzang.jeongsan.usecase

import android.graphics.Bitmap
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.repository.ReceiptCaptureRepository
import com.kappzzang.jeongsan.util.Base64BitmapEncoder
import javax.inject.Inject

class AnalyzeReceiptImageUseCase @Inject constructor(
    private val receiptCaptureRepository: ReceiptCaptureRepository
) {
    suspend operator fun invoke(imageBitmap: Bitmap): OcrResultResponse {
        val encoded = Base64BitmapEncoder.convertBitmapToBase64String(imageBitmap)

        return receiptCaptureRepository.getAnalyzedReceiptImage(encoded)
    }
}
