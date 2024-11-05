package com.kappzzang.jeongsan.expensedetail

import android.util.Log
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.ExpenseItemWithDetails
import com.kappzzang.jeongsan.usecase.EditExpenseDetailUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseDetailUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
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
class ExpenseDetailViewModelTest {
    private val getExpenseDetailUseCase = mockk<GetExpenseDetailUseCase>(relaxed = true)
    private val editExpenseDetailUseCase = mockk<EditExpenseDetailUseCase>()
    private lateinit var viewModel: ExpenseDetailViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        Dispatchers.setMain(testDispatcher)

        viewModel = ExpenseDetailViewModel(
            getExpenseDetailUseCase = getExpenseDetailUseCase,
            editExpenseDetailUseCase = editExpenseDetailUseCase,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `아이템 해제 시 비활성화 처리`() = runTest {
        coEvery { getExpenseDetailUseCase(any()) } returns Result.success(
            ExpenseItemWithDetails(
                item = ExpenseItem.EMPTY,
                expenseImageUrl = "",
                expenseDetails = listOf(
                    ExpenseDetailItem("testId", "testItem", 100, 30, 10)
                )
            )
        )

        viewModel.setInitialData("testId", "", true)
        advanceUntilIdle()

        viewModel.updateItemCheck(false, 0)
        advanceUntilIdle()

        assertEquals(0, viewModel.expenseDetailList.value[0].selectedQuantity)
    }

    @Test
    fun `아이템 개수 변경시 업데이트`() = runTest {
        coEvery { getExpenseDetailUseCase(any()) } returns Result.success(
            ExpenseItemWithDetails(
                item = ExpenseItem.EMPTY,
                expenseImageUrl = "",
                expenseDetails = listOf(
                    ExpenseDetailItem("testId", "testItem", 100, 30, 10)
                )
            )
        )
        viewModel.setInitialData("testId", "", true)
        advanceUntilIdle()

        val testSelected = 10
        viewModel.updateSelectedQuantity(testSelected, 0)
        advanceUntilIdle()

        assertEquals(testSelected, viewModel.expenseDetailList.value[0].selectedQuantity)
    }
}
