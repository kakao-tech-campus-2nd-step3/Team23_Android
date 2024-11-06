package com.kappzzang.jeongsan.addexpense

import android.graphics.Bitmap
import com.kappzzang.jeongsan.model.OcrDetailItem
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.model.ReceiptItem
import com.kappzzang.jeongsan.usecase.GetCategoryListUseCase
import com.kappzzang.jeongsan.usecase.UploadExpenseUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.unmockkAll
import java.time.LocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AddExpenseViewModelTest {

    private val mockUploadExpenseUseCase = mockk<UploadExpenseUseCase>()
    private val mockGetCategoryListUseCase = mockk<GetCategoryListUseCase>()
    private lateinit var viewModel: AddExpenseViewModel

    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { mockGetCategoryListUseCase() } returns Result.success(emptyList())
        viewModel = spyk(AddExpenseViewModel(mockUploadExpenseUseCase, testDispatcher, mockGetCategoryListUseCase))
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `영수증 모드인 경우 해당 속성을 반영하는지 확인`() = runTest {
        // Given
        val manualMode = AddExpenseViewModel.Companion.ManualMode.RECEIPT

        // When
        viewModel.setManualMode(manualMode)
        advanceUntilIdle()

        // Then
        assertEquals(false, viewModel.manualMode.value)
        assertEquals(true, viewModel.uploadedImage.value)
    }

    @Test
    fun `수기 입력 모드인 경우 해당 속성을 반영하는지 확인`() = runTest {
        // Given
        val testManualMode = AddExpenseViewModel.Companion.ManualMode.MANUAL

        // When
        viewModel.setManualMode(testManualMode)
        advanceUntilIdle()

        // Then
        assertEquals(true, viewModel.manualMode.value)
        assertEquals(false, viewModel.uploadedImage.value)
    }

    @Test
    fun `영수증 인식 정보를 잘 반영하는지 확인`() = runTest {
        // Given
        val testBitmap = mockk<Bitmap>()
        val testOcrResult = OcrResultResponse.OcrSuccess(
            name = "Test Receipt",
            paymentTime = LocalDateTime.now(),
            detailItems = listOf(
                OcrDetailItem("Test Item 1", 1000, 1),
                OcrDetailItem("Test Item 2", 2000, 2)
            )
        )

        // When
        viewModel.setInitialReceiptData(testBitmap, testOcrResult)
        advanceUntilIdle()

        // Then
        assertEquals(testBitmap, viewModel.expenseImageBitmap.value)
        assertEquals(testOcrResult.name, viewModel.expenseName.value)
        assertEquals(testOcrResult.detailItems.size + 1, viewModel.expenseItemList.value.size)
        for (i in testOcrResult.detailItems.indices) {
            assertEquals(
                testOcrResult.detailItems[i].itemName,
                viewModel.expenseItemList.value[i].itemName
            )
            assertEquals(
                testOcrResult.detailItems[i].itemPrice,
                viewModel.expenseItemList.value[i].itemPrice
            )
            assertEquals(
                testOcrResult.detailItems[i].itemQuantity,
                viewModel.expenseItemList.value[i].itemQuantity
            )
        }
    }

    @Test
    fun `항목 추가를 진행할 때 정상적으로 늘어나는지 확인`() = runTest {
        // Given
        val initialItemSize = viewModel.expenseItemList.value.size

        // When
        viewModel.addNewExpense()
        advanceUntilIdle()

        // Then
        val afterItemSize = viewModel.expenseItemList.value.size
        assertEquals(initialItemSize + 1, afterItemSize)
        assertEquals(false, viewModel.expenseItemList.value[afterItemSize - 2].isPlaceholder)
        assertEquals(true, viewModel.expenseItemList.value[afterItemSize - 1].isPlaceholder)
    }

    @Test
    fun `항목 삭제를 진행할 때 정상적으로 삭제되는지 확인`() = runTest {
        // Given
        viewModel.addNewExpense()
        advanceUntilIdle()
        val initialItemSize = viewModel.expenseItemList.value.size

        // When
        viewModel.removeExpense(0)
        advanceUntilIdle()

        // Then
        assertEquals(initialItemSize - 1, viewModel.expenseItemList.value.size)
    }

    @Test
    fun `빈 값들을 업로드 할때, 이를 잘 검사하는지 확인`() = runTest {
        // Given

        // When
        val result = viewModel.uploadExpense()
        advanceUntilIdle()

        // Then
        assertEquals(false, result)
    }

    @Test
    fun `유효한 값들을 업로드 할 때, 잘 실행하는지 확인`() = runTest {
        // Given
        val testBitmap = mockk<Bitmap>()
        val testBase64 = "test_base64"
        every { viewModel.convertBitmapToBase64(any()) } returns testBase64
        coEvery { mockUploadExpenseUseCase(any(), any()) } returns Result.success("test success")

        val testOcrResult = OcrResultResponse.OcrSuccess(
            name = "Test Receipt",
            paymentTime = LocalDateTime.now(),
            detailItems = listOf(
                OcrDetailItem("Test Item 1", 1000, 1),
                OcrDetailItem("Test Item 2", 2000, 2)
            )
        )
        viewModel.setInitialReceiptData(testBitmap, testOcrResult)
        advanceUntilIdle()

        // When
        val result = viewModel.uploadExpense()
        advanceUntilIdle()

        // Then
        assertEquals(true, result)
        val receiptItemSlot = slot<ReceiptItem>()
        coVerify { mockUploadExpenseUseCase(capture(receiptItemSlot), any()) }
        assertEquals(testOcrResult.name, receiptItemSlot.captured.title)
        assertEquals(testBase64, receiptItemSlot.captured.imageBase64)
        assertEquals(
            testOcrResult.detailItems.size,
            receiptItemSlot.captured.expenseDetailItemList.size
        )
        assertEquals(
            testOcrResult.detailItems[0].itemName,
            receiptItemSlot.captured.expenseDetailItemList[0].itemName
        )
    }
}
