package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.api.OcrRetrofitService
import com.kappzzang.jeongsan.entity.ReceiptAnalyzeResponse
import com.kappzzang.jeongsan.entity.ReceiptImage
import javax.inject.Inject

class ReceiptCaptureRemoteDatasource @Inject constructor(
    private val ocrRetrofitService: OcrRetrofitService
) {
    suspend fun analyzeReceipt(base64Encoded: String): Result<ReceiptAnalyzeResponse> {
        try {
            val response = ocrRetrofitService.analyzeReceipt(
                receiptImage = ReceiptImage(
                    format = "JPEG",
                    name = "empty",
                    base64Encoded = base64Encoded,
                    url = null
                )
            )
            return if (!response.isSuccessful || response.body()?.data?.items?.isNotEmpty() != true) {
                Result.failure(Exception(""))
            } else {
                Result.success(response.body()!!.data)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
