package com.kappzzang.jeongsan.sendmessage

import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.sendmessage.data.HasTransferInfo
import com.kappzzang.jeongsan.sendmessage.data.TransferInfoUIState
import com.kappzzang.jeongsan.usecase.GetPurchasedExpenseListUseCase
import com.kappzzang.jeongsan.usecase.GetTransferInfoUseCase
import com.kappzzang.jeongsan.usecase.SendTransferMessageUseCase
import com.kappzzang.jeongsan.usecase.SetExpensesToCompleteUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
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
class SendMessageViewModelTest {

    private val mockGetTransferInfoUseCase = mockk<GetTransferInfoUseCase>()
    private val mockSendTransferMessageUseCase = mockk<SendTransferMessageUseCase>()
    private lateinit var viewModel: SendMessageViewModel
    private val mockGetPurchasedExpenseListUseCase = mockk<GetPurchasedExpenseListUseCase>()
    private val mockSetExpensesToCompleteUseCase = mockk<SetExpensesToCompleteUseCase>()
    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { mockGetTransferInfoUseCase(any(), any()) } returns Result.success(emptyList())
        coEvery { mockSendTransferMessageUseCase(any()) } returns Result.success(Unit)
        viewModel = SendMessageViewModel(
            mockGetTransferInfoUseCase,
            mockSendTransferMessageUseCase,
            mockGetPurchasedExpenseListUseCase,
            mockSetExpensesToCompleteUseCase,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `송금 정보가 올바르게 받아서 합계를 잘 계산하는지 확인`() = runTest {
        // given
        val expectedTransferInfo = listOf(
            TransferDetailItem("1", "12", "test item 1", 2024, "test image url 1"),
            TransferDetailItem("2", "22", "test item 2", 11, "test image url 2"),
            TransferDetailItem("3", "32", "test item 3", 1, "test image url 3")
        )
        coEvery { mockGetTransferInfoUseCase(any(), any()) } returns Result.success(
            expectedTransferInfo
        )

        coEvery { mockSendTransferMessageUseCase(any()) } returns Result.success(Unit)
        coEvery { mockGetPurchasedExpenseListUseCase(any()) } returns Result.success(
            listOf(
                ExpenseItem("", "", 100, ExpenseState.TRANSFER_PENDING)
            )
        )

        // when
        viewModel = SendMessageViewModel(
            mockGetTransferInfoUseCase,
            mockSendTransferMessageUseCase,
            mockGetPurchasedExpenseListUseCase,
            mockSetExpensesToCompleteUseCase,
            testDispatcher
        )
        viewModel.setGroupId("123")
        viewModel.startFetchUiState()

        advanceUntilIdle()

        // then
        val expectedTotalPrice = 2036
        assertEquals(
            expectedTransferInfo,
            (viewModel.transferInfoState.value as? HasTransferInfo)?.transferInfoList
        )
        assertEquals(
            expectedTotalPrice,
            (viewModel.transferInfoState.value as? HasTransferInfo)?.totalExpenseToGet
        )
    }

    @Test
    fun `소비 내역 조회 실패 시 상태가 PurchaseListGetError로 변경되는지 확인`() = runTest {
        // Given
        val errorMessage = "소비 내역 조회 실패"
        coEvery { mockGetPurchasedExpenseListUseCase(any()) } returns Result.failure(
            Exception(errorMessage)
        )
        viewModel.setGroupId("test_group_id")

        // When
        viewModel.startFetchUiState()
        advanceUntilIdle()

        // Then
        assertEquals(
            "결제한 지출 정보를 불러오는 데 실패했습니다.\n$errorMessage",
            (viewModel.transferInfoState.value as TransferInfoUIState.PurchaseListGetError).message
        )
    }

    @Test
    fun `송금 요청 메시지 전송 실패 시 상태가 TransferMessageSendError로 변경되는지 확인`() = runTest {
        // Given
        val errorMessage = "송금 메시지 전송 실패"
        coEvery { mockGetPurchasedExpenseListUseCase(any()) } returns Result.success(
            listOf(ExpenseItem("", "", 100, ExpenseState.TRANSFER_PENDING))
        )
        coEvery { mockGetTransferInfoUseCase(any(), any()) } returns Result.success(
            listOf(TransferDetailItem("1", "12", "item", 100, "url"))
        )
        coEvery { mockSendTransferMessageUseCase(any()) } returns Result.failure(
            Exception(errorMessage)
        )
        viewModel.setGroupId("test_group_id")
        viewModel.startFetchUiState()
        advanceUntilIdle()

        // When
        viewModel.sendTransferMessage()
        advanceUntilIdle()

        // Then
        assertEquals(
            "송금 요청 메시지 전송을 실패했습니다: $errorMessage",
            (viewModel.transferInfoState.value as TransferInfoUIState.TransferMessageSendError)
                .message
        )
    }
}
