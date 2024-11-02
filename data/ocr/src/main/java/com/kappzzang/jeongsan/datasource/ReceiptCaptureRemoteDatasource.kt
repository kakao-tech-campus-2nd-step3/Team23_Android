package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.api.OcrRetrofitService
import com.kappzzang.jeongsan.entity.ReceiptAnalyzeResponse
import com.kappzzang.jeongsan.entity.ReceiptImage
import javax.inject.Inject
import retrofit2.Response

class ReceiptCaptureRemoteDatasource @Inject constructor(
    private val ocrRetrofitService: OcrRetrofitService
) {
    suspend fun analyzeReceipt(
        jwt: String,
        base64Encoded: String
    ): Response<ReceiptAnalyzeResponse> = ocrRetrofitService.analyzeReceipt(
        token = jwt,
        receiptImage = ReceiptImage(
            format = "",
            name = "",
            base64Encoded = base64Encoded,
            url = ""
        )
    )
}
