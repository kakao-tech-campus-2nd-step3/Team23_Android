package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.ReceiptCaptureFakeDatasource
import com.kappzzang.jeongsan.mapper.OcrResultEntityMapper
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.repository.ReceiptCaptureRepository
import javax.inject.Inject

class ReceiptCaptureRepositoryImpl @Inject constructor(
    private val dataSource: ReceiptCaptureFakeDatasource
) : ReceiptCaptureRepository {

    override suspend fun getAnalyzedReceiptImage(encodedReceiptImage: String): OcrResultResponse {
        val result = dataSource.sendToOcrServerAndGetResult(encodedReceiptImage)

        return OcrResultEntityMapper.mapOcrResultEntityToModel(result)
    }
}
