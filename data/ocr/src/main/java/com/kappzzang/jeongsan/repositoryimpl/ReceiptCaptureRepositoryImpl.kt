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
                try {
                    OcrResultEntityMapper.mapOcrResultEntityToModel(it)
                } catch (e: Exception) {
                    OcrResultResponse.OcrFailed("영수증 데이터를 인식하는 중 오류가 발생했습니다.", 0)
                }
            },
            onFailure = {
                OcrResultResponse.OcrFailed(it.message ?: "", 0)
            }
        )
    }
}
