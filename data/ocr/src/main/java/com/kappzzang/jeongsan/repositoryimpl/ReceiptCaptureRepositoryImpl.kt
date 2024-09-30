package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.domain.model.OcrResultResponse
import com.kappzzang.jeongsan.domain.repository.ReceiptCaptureRepository
import javax.inject.Inject

class ReceiptCaptureRepositoryImpl @Inject constructor(
    private val dataSource: com.kappzzang.jeongsan.datasource.ReceiptCaptureFakeDatasource
) : ReceiptCaptureRepository {

    override suspend fun getAnalyzedReceiptImage(encodedReceiptImage: String): OcrResultResponse {
        val result = dataSource.sendToOcrServerAndGetResult(encodedReceiptImage)

        return com.kappzzang.jeongsan.mapper.OcrResultEntityMapper.mapOcrResultEntityToModel(result)
    }
}
