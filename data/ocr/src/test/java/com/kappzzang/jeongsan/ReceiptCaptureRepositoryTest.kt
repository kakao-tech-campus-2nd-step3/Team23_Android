package com.kappzzang.jeongsan

import com.kappzzang.jeongsan.datasource.ReceiptCaptureFakeDatasource
import com.kappzzang.jeongsan.mapper.OcrResultEntityMapper
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.repository.ReceiptCaptureRepository
import com.kappzzang.jeongsan.repositoryimpl.ReceiptCaptureRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ReceiptCaptureRepositoryTest {
    private lateinit var repository: ReceiptCaptureRepository
    private val receiptCaptureFakeDatasource = ReceiptCaptureFakeDatasource()

    @Before
    fun setUp() {
        repository = ReceiptCaptureRepositoryImpl(
            receiptCaptureFakeDatasource
        )
    }

    @Test
    fun `값을 입력 받으면 Response를 리턴해야 한다`() = runTest {
        // given
        val encodedImage = "encoded_image"

        // when
        val result = repository.getAnalyzedReceiptImage(encodedImage)

        // then
        assertThat(result is OcrResultResponse.OcrSuccess).isTrue()
    }

    @Test
    fun `리포지토리는 DataSource로부터 받은 데이터를 도메인 모델로 매핑하여 리턴해야 한다`() = runTest {
        // given
        val encodedImage = "encoded_image"
        val originalData = receiptCaptureFakeDatasource.sendToOcrServerAndGetResult(encodedImage)
        val mappedData = OcrResultEntityMapper.mapOcrResultEntityToModel(originalData)

        // when
        val result = repository.getAnalyzedReceiptImage(encodedImage)
        val firstResponseItem = result as? OcrResultResponse.OcrSuccess

        // then
        assertThat(firstResponseItem).isEqualTo(mappedData)
    }
}
