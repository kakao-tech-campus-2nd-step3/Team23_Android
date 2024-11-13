package com.kappzzang.jeongsan.expenselist

import android.util.Log
import com.kappzzang.jeongsan.data.ExpenseListUIState
import com.kappzzang.jeongsan.expenselist.viewmodel.ExpenseListOnCalculationPageViewModel
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.ExpenseItemWithCategory
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.usecase.ForceFetchExpenseListUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseListUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import java.time.LocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
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
class ExpenseListFragmentViewModelTest {
    private val getExpenseListUseCase = mockk<GetExpenseListUseCase>(relaxed = true)
    private val forceFetchExpenseListUseCase = mockk<ForceFetchExpenseListUseCase>(relaxed = true)
    private lateinit var viewModel: ExpenseListOnCalculationPageViewModel
    private val testDispatcher = StandardTestDispatcher()

    private fun createFakeItem(state: ExpenseState = ExpenseState.NOT_CONFIRMED) =
        ExpenseItem("", "", 0, state)

    private fun createFakeExpenseItemList(state: ExpenseState) = listOf(
        ExpenseItemWithCategory(
            item = ExpenseItem(
                name = "item1",
                id = "id1",
                price = 100,
                state = state
            ),
            personalExpense = 100,
            isMyPayment = true,
            date = LocalDateTime.of(2000, 10, 1 + state.ordinal, 10, 10),
            categoryColor = "#ff00ff"
        ),

        ExpenseItemWithCategory(
            item = ExpenseItem(
                name = "item2",
                id = "id2",
                price = 150,
                state = state
            ),
            personalExpense = 50,
            isMyPayment = false,
            date = LocalDateTime.of(2020, 5, 1 + state.ordinal, 7, 8),
            categoryColor = "#0ff0f0"
        )

    )

    private fun mockGetExpenseListUseCase() {
        val expenseStateSlot = slot<ExpenseState>()
        every { getExpenseListUseCase(any(), capture(expenseStateSlot)) } returns flow {
            emit(
                Result.success(
                    ExpenseListResponse(
                        totalPrice = 1,
                        totalExpenseToSend = 0,
                        expenseList = createFakeExpenseItemList(expenseStateSlot.captured)
                    )
                )
            )
        }
    }

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        Dispatchers.setMain(testDispatcher)

        viewModel = ExpenseListOnCalculationPageViewModel(
            ioDispatcher = testDispatcher,
            getExpenseListUseCase = getExpenseListUseCase,
            forceFetchExpenseListUseCase = forceFetchExpenseListUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `state를 Inject한 후 액티비티의 UIState에서 groupId를 감지하면 getExpenseUseCase가 호출된다`() = runTest {
        // given
        val stateFlow = MutableStateFlow<ExpenseListUIState>(ExpenseListUIState.Initial)
        val testGroupId = "groupId"
        mockGetExpenseListUseCase()

        // when
        viewModel.injectActivityViewModelState(stateFlow)
        stateFlow.value = ExpenseListUIState.Idle(
            groupId = testGroupId,
            groupSubject = "",
            groupName = ""
        )
        advanceUntilIdle()

        // then
        verify { getExpenseListUseCase(groupId = testGroupId, any()) }
    }

    private fun initiateInjection(groupId: String = "0") {
        val stateFlow = MutableStateFlow<ExpenseListUIState>(ExpenseListUIState.Initial)
        viewModel.injectActivityViewModelState(stateFlow)
        stateFlow.value = ExpenseListUIState.Idle(
            groupId = groupId,
            groupSubject = "",
            groupName = ""
        )
    }

    @Test
    fun `프래그먼트가 새로고침 되면 getExpenseUseCase가 호출된다`() = runTest {
        // when
        mockGetExpenseListUseCase()
        initiateInjection()
        viewModel.onFragmentReload()

        advanceUntilIdle()

        // then
        verify(exactly = 2) { getExpenseListUseCase(any(), any()) }
    }

    @Test
    fun `불러온 지출 정보가 UI 데이터에 정상적으로 포함된다`() = runTest {
        // given
        backgroundScope.launch(StandardTestDispatcher(testScheduler)) {
            viewModel.uiData.collect()
        }
        assertThat(viewModel.uiData.value.expenseItems).isEmpty()

        // when
        mockGetExpenseListUseCase()
        initiateInjection()

        advanceUntilIdle()

        // then
        assertThat(viewModel.uiData.value.expenseItems).isNotEmpty()
    }

    @Test
    fun `불러온 지출 정보는 날짜의 내림차순으로 정렬된다`() = runTest {
        // given
        backgroundScope.launch(StandardTestDispatcher(testScheduler)) {
            viewModel.uiData.collect()
        }
        val definitelyNotSortedList = listOf(
            ExpenseItemWithCategory(
                item = createFakeItem(),
                personalExpense = 0,
                isMyPayment = false,
                categoryColor = "ff2222",
                date = LocalDateTime.of(2023, 10, 10, 10, 10)
            ),

            ExpenseItemWithCategory(
                item = createFakeItem(),
                personalExpense = 0,
                isMyPayment = false,
                categoryColor = "ff2222",
                date = LocalDateTime.of(2024, 8, 7, 6, 2)
            ),

            ExpenseItemWithCategory(
                item = createFakeItem(),
                personalExpense = 0,
                isMyPayment = false,
                categoryColor = "ff2222",
                date = LocalDateTime.of(2010, 12, 3, 7, 5)
            )
        )

        every { getExpenseListUseCase(any(), any()) } returns flow {
            emit(
                Result.success(
                    ExpenseListResponse(
                        expenseList = definitelyNotSortedList,
                        totalPrice = 0,
                        totalExpenseToSend = 0
                    )
                )
            )
        }

        // when
        initiateInjection()

        advanceUntilIdle()

        // then
        val expenseItemsCreatedTime = viewModel.uiData.value.expenseItems.map {
            it.date
        }
        assertThat(expenseItemsCreatedTime.reversed())
            .isNotEmpty
            .isSorted
    }
}
