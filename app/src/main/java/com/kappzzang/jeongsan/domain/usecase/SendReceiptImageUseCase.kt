package com.kappzzang.jeongsan.domain.usecase

import android.graphics.Bitmap
import com.kappzzang.jeongsan.domain.model.OcrResultResponse
import com.kappzzang.jeongsan.domain.repository.ReceiptCaptureRepository
import com.kappzzang.jeongsan.util.Base64BitmapEncoder
import javax.inject.Inject

class SendReceiptImageUseCase @Inject constructor(private val receiptCaptureRepository: ReceiptCaptureRepository) {
    suspend operator fun invoke(imageBitmap: Bitmap): OcrResultResponse {
        val encoded = Base64BitmapEncoder.convertBitmapToBase64String(imageBitmap)

        return receiptCaptureRepository.getOcrImage(encoded)
    }
}
