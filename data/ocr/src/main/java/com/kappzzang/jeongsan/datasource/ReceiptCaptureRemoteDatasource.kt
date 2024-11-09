package com.kappzzang.jeongsan.datasource

import android.util.Log
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
                    format = "",
                    name = "",
                    base64Encoded = base64Encoded,
                    url = ""
                )
            )
            return if(!response.isSuccessful){
                Result.failure(Exception(""))
            } else{
                Result.success(response.body()!!)
            }
        }
        catch (e:Exception) {
            return Result.failure(e)
        }
    }
}
