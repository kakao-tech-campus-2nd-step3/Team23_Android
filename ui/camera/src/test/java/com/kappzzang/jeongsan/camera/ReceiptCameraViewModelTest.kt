package com.kappzzang.jeongsan.camera

import android.app.Application
import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.usecase.AnalyzeReceiptImageUseCase
import com.kappzzang.jeongsan.util.Base64BitmapEncoder
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ReceiptCameraViewModelTest {
    private val application = mockk<Application>(relaxed = true)
    private val analyzeReceiptImageUseCase = mockk<AnalyzeReceiptImageUseCase>()
    private lateinit var viewModel: ReceiptCameraViewModel
    private val contentResolver = mockk<ContentResolver>()

    private val testDispatcher = StandardTestDispatcher()
    private val testUri = mockk<Uri>(relaxed = true)
    private val testBitmap = mockk<Bitmap>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { application.contentResolver } returns contentResolver
        every { application.applicationContext } returns application

        mockkStatic(MediaStore.Images.Media::class)
        every { MediaStore.Images.Media.getBitmap(contentResolver, any()) } returns testBitmap

        mockkStatic(Base64BitmapEncoder::class)
        every { Base64BitmapEncoder.convertUriToBitmap(any(), application) } returns testBitmap

        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns testUri

        viewModel = ReceiptCameraViewModel(application, analyzeReceiptImageUseCase, testDispatcher)
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `서버에 사진 전송 - 성공 응답 시 serverResponse를 설정하고 상태를 RECEIVE_SERVER_RESPONSE로 변경`() = runTest {
        // given
        val mockResponse = mockk<OcrResultResponse.OcrSuccess>()
        coEvery { analyzeReceiptImageUseCase(any()) } returns mockResponse

        // when
        viewModel.setPictureData(testUri)
        advanceUntilIdle()

        // then
        assertEquals(mockResponse, viewModel.serverResponse)
        assertEquals(
            ReceiptCameraViewModel.ReceiptPictureState.RECEIVE_SERVER_RESPONSE,
            viewModel.receiptPictureState.value
        )
    }

    @Test
    fun `서버에 사진 전송 - 실패 응답 시 오류 메시지를 설정하고 상태를 ERROR로 변경`() = runTest {
        // given
        val errorMessage = "OCR Failed"
        val errorCode = 0
        val testResponse = OcrResultResponse.OcrFailed(errorMessage, errorCode)
        coEvery { analyzeReceiptImageUseCase(any()) } returns testResponse

        // when
        viewModel.setPictureData(testUri)
        advanceUntilIdle()

        // then
        assertEquals(errorMessage, viewModel.serverErrorMessage)
        assertEquals(
            ReceiptCameraViewModel.ReceiptPictureState.ERROR,
            viewModel.receiptPictureState.value
        )
    }

    @Test
    fun `서버에 사진 전송 - IOException 발생 시 오류 메시지를 설정하고 상태를 ERROR로 변경`() = runTest {
        // given
        val errorMessage = "IO Exception Error Message"
        every { Base64BitmapEncoder.convertUriToBitmap(testUri, application) } throws IOException(
            errorMessage
        )

        // when
        viewModel.setPictureData(testUri)
        advanceUntilIdle()

        // then
        assertEquals(errorMessage, viewModel.serverErrorMessage)
        assertEquals(
            ReceiptCameraViewModel.ReceiptPictureState.ERROR,
            viewModel.receiptPictureState.value
        )
    }
}
