package com.kappzzang.jeongsan.api

import com.kappzzang.jeongsan.entity.ReceiptAnalyzeResponse
import com.kappzzang.jeongsan.entity.ReceiptImage
import com.kappzzang.jeongsan.retrofit.ResponseData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OcrRetrofitService {
    @POST("/api/receipts/analyze")
    suspend fun analyzeReceipt(
        @Body receiptImage: ReceiptImage
    ): Response<ResponseData<ReceiptAnalyzeResponse>>
}
