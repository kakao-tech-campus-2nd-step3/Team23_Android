package com.kappzzang.jeongsan.expenselist.sendmessage

import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.usecase.GetTransferInfoUseCase
import com.kappzzang.jeongsan.usecase.SendTransferMessageUseCase
import io.mockk.coEvery
import io.mockk.coVerify
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

    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { mockGetTransferInfoUseCase() } returns emptyList()
        coEvery { mockSendTransferMessageUseCase(any()) } returns true
        viewModel = SendMessageViewModel(mockGetTransferInfoUseCase, mockSendTransferMessageUseCase)
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
            TransferDetailItem("1", "test item 1", 2024, "test image url 1"),
            TransferDetailItem("2", "test item 2", 11, "test image url 2"),
            TransferDetailItem("3", "test item 3", 1, "test image url 3")
        )
        coEvery { mockGetTransferInfoUseCase() } returns expectedTransferInfo

        // when
        viewModel = SendMessageViewModel(mockGetTransferInfoUseCase, mockSendTransferMessageUseCase)
        advanceUntilIdle()

        // then
        val expectedTotalPrice = 2036
        assertEquals(expectedTransferInfo, viewModel.transferInfo.value)
        assertEquals(expectedTotalPrice, viewModel.totalPrice.value)
    }

    @Test
    fun `sendTransferMessage가 성공적으로 호출되는지 확인`() = runTest {
        // given
        val transferInfo = viewModel.transferInfo.value

        // when
        val result = viewModel.sendTransferMessage()
        advanceUntilIdle()

        // then
        assertEquals(true, result)
        coVerify { mockSendTransferMessageUseCase(transferInfo) }
    }
}
