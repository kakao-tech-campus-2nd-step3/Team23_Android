package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.ReceiptCaptureRemoteDatasource
import com.kappzzang.jeongsan.mapper.OcrResultEntityMapper
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.repository.ReceiptCaptureRepository
import javax.inject.Inject

class ReceiptCaptureRepositoryImpl @Inject constructor(
    private val dataSource: ReceiptCaptureRemoteDatasource
) : ReceiptCaptureRepository {

    override suspend fun getAnalyzedReceiptImage(encodedReceiptImage: String): OcrResultResponse {
        val result = dataSource.analyzeReceipt(encodedReceiptImage)
        return result.fold(
            onSuccess = {
                OcrResultEntityMapper.mapOcrResultEntityToModel(it)
            },
            onFailure = {
                OcrResultResponse.OcrFailed(it.message ?: "", 0)
            }
        )
    }
}
