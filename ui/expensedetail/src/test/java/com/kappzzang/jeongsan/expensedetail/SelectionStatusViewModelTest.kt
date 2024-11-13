package com.kappzzang.jeongsan.expensedetail

import android.util.Log
import com.kappzzang.jeongsan.data.SelectionInfoItem
import com.kappzzang.jeongsan.expensedetail.selectionstatus.SelectionStatusViewModel
import com.kappzzang.jeongsan.model.ExpenseSelectionStatus
import com.kappzzang.jeongsan.model.ExpenseSelectionStatusItem
import com.kappzzang.jeongsan.model.ExpenseSelectorInfo
import com.kappzzang.jeongsan.usecase.GetExpenseSelectionStatusUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SelectionStatusViewModelTest {
    private val expenseSelectionStatusUseCase =
        mockk<GetExpenseSelectionStatusUseCase>(relaxed = true)
    private lateinit var viewModel: SelectionStatusViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        Dispatchers.setMain(testDispatcher)

        viewModel = SelectionStatusViewModel(
            testDispatcher,
            expenseSelectionStatusUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `ExpenseId를 초기화 할 때 선택 정보를 불러오는 UseCase가 호출된다`() = runTest {
        // given
        coEvery { expenseSelectionStatusUseCase(any()) } returns Result.success(
            ExpenseSelectionStatus(
                name = "expenseName",
                items = emptyList()
            )
        )
        val expenseId = "1122"

        // when
        viewModel.initExpenseId(expenseId)
        advanceUntilIdle()

        // then
        coVerify {
            expenseSelectionStatusUseCase(expenseId)
        }
    }

    private fun getTestExpenseSelectionItemList() =
        listOf(
            ExpenseSelectionStatusItem(
                itemId = "id1",
                unitPrice = 200,
                quantity = 10,
                name = "name1",
                selectorList = listOf(
                    ExpenseSelectorInfo(
                        name = "selectorName",
                        selectedQuantity = 2,
                        profileImageUrl = "url"
                    ),
                    ExpenseSelectorInfo(
                        name = "selectorName2",
                        selectedQuantity = 8,
                        profileImageUrl = "url"
                    )
                )
            ),
            ExpenseSelectionStatusItem(
                itemId = "id2",
                unitPrice = 400,
                quantity = 20,
                name = "name2",
                selectorList = listOf(
                    ExpenseSelectorInfo(
                        name = "selectorName",
                        selectedQuantity = 2,
                        profileImageUrl = "url"
                    )
                )
            )
        )

    @Test
    fun `UI State 내 헤더의 개수는 Usecase에서 받은 아이템의 개수와 같다`() = runTest {
        backgroundScope.launch(StandardTestDispatcher(testScheduler)) {
            viewModel.uiData.collect()
        }

        // given
        val expenseName = "name"
        val itemList = getTestExpenseSelectionItemList()

        coEvery { expenseSelectionStatusUseCase(any()) } returns Result.success(
            ExpenseSelectionStatus(
                name = expenseName,
                items = itemList
            )
        )

        // when
        viewModel.initExpenseId("123")
        advanceUntilIdle()

        // then
        val headerCount = viewModel.uiData.value.selectionInfoItemList.count {
            it is SelectionInfoItem.Header
        }

        assertThat(headerCount).isEqualTo(itemList.size)
    }

    @Test
    fun `UI State 내 SelectorItem의 개수는 Usecase에서 받은 selector의 개수와 같다`() = runTest {
        backgroundScope.launch(StandardTestDispatcher(testScheduler)) {
            viewModel.uiData.collect()
        }

        // given
        val expenseName = "name"
        val itemList = getTestExpenseSelectionItemList()

        coEvery { expenseSelectionStatusUseCase(any()) } returns Result.success(
            ExpenseSelectionStatus(
                name = expenseName,
                items = itemList
            )
        )

        // when
        viewModel.initExpenseId("123")
        advanceUntilIdle()

        // then
        val selectionInfoItemCount = viewModel.uiData.value.selectionInfoItemList.count {
            it is SelectionInfoItem.SelectorItem
        }

        assertThat(selectionInfoItemCount).isEqualTo(itemList.sumOf { it.selectorList.size })
    }

    @Test
    fun `각 아이템의 사이에 Divider가 들어간다`() = runTest {
        backgroundScope.launch(StandardTestDispatcher(testScheduler)) {
            viewModel.uiData.collect()
        }

        // given
        val expenseName = "name"
        val itemList = getTestExpenseSelectionItemList()

        coEvery { expenseSelectionStatusUseCase(any()) } returns Result.success(
            ExpenseSelectionStatus(
                name = expenseName,
                items = itemList
            )
        )

        // when
        viewModel.initExpenseId("123")
        advanceUntilIdle()

        // then
        val uiItemList = viewModel.uiData.value.selectionInfoItemList
        for (i in 2 until uiItemList.size){
            if(uiItemList[i] is SelectionInfoItem.Header){
                assertThat(uiItemList[i - 1]).isInstanceOf(SelectionInfoItem.Divider::class.java)
                assertThat(uiItemList[i - 2]).isInstanceOf(SelectionInfoItem.SelectorItem::class.java)
            }
        }
    }
}
