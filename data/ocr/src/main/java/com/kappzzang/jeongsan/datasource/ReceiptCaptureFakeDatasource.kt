package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.data.entity.OcrResultEntity
import com.kappzzang.jeongsan.data.entity.OcrResultItem
import javax.inject.Inject
import kotlinx.coroutines.delay

class ReceiptCaptureFakeDatasource @Inject constructor() {
    suspend fun sendToOcrServerAndGetResult(encodedImage: String): OcrResultEntity {
        delay(2000)

        val result = OcrResultEntity(
            title = "Test Success Result",
            paymentTime = "2020-04-16 20:11:00",
            items = listOf(
                OcrResultItem(
                    name = "감자",
                    quantity = 4,
                    unitPrice = 1200
                ),
                OcrResultItem(
                    name = "토마토",
                    quantity = 1,
                    unitPrice = 1500
                ),
                OcrResultItem(
                    name = "당근",
                    quantity = 10,
                    unitPrice = 400
                ),
                OcrResultItem(
                    name = "양배추",
                    quantity = 2,
                    unitPrice = 3000
                )
            )
        )

        return result
    }
}
