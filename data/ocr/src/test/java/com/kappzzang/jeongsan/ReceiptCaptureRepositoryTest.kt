package com.kappzzang.jeongsan

import com.kappzzang.jeongsan.datasource.ReceiptCaptureRemoteDatasource
import com.kappzzang.jeongsan.entity.ReceiptAnalyzeResponse
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.repository.ReceiptCaptureRepository
import com.kappzzang.jeongsan.repositoryimpl.ReceiptCaptureRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ReceiptCaptureRepositoryTest {
    private lateinit var repository: ReceiptCaptureRepository
    private val mockedDatasource = mockk<ReceiptCaptureRemoteDatasource>(relaxed = true)

    @Before
    fun setUp() {
        repository = ReceiptCaptureRepositoryImpl(
            mockedDatasource
        )
    }

    @Test
    fun `값을 입력 받으면 Response를 리턴해야 한다`() = runTest {
        // given
        coEvery { mockedDatasource.analyzeReceipt(any()) } returns Result.success(
            ReceiptAnalyzeResponse(
                "Success Result",
                "2024-11-11T05:28:36.643Z",
                items = listOf()
            )
        )
        val encodedImage = "encoded_image"

        // when
        val result = repository.getAnalyzedReceiptImage(encodedImage)
        val firstResponseItem = result as? OcrResultResponse.OcrSuccess

        // then
        assertThat(firstResponseItem is OcrResultResponse.OcrSuccess).isEqualTo(true)
    }
}
