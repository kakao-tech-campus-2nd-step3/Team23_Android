package com.kappzzang.jeongsan.data.repositoryimpl

import com.kappzzang.jeongsan.data.datasource.ReceiptCaptureFakeDatasource
import com.kappzzang.jeongsan.data.mapper.OcrResultEntityMapper
import com.kappzzang.jeongsan.domain.model.OcrResultResponse
import com.kappzzang.jeongsan.domain.repository.ReceiptCaptureRepository
import javax.inject.Inject

class ReceiptCaptureRepositoryImpl @Inject constructor(
    private val dataSource: ReceiptCaptureFakeDatasource
) : ReceiptCaptureRepository {

    override suspend fun getAnalyzedReceiptImage(encodedReceiptImage: String): OcrResultResponse {
        val result = dataSource.sendToOcrServerAndGetResult(encodedReceiptImage)

        return OcrResultEntityMapper.mapOcrResultEntityToModel(result)
    }
}
