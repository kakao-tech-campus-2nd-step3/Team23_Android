package com.kappzzang.jeongsan.expensedetail

import android.util.Log
import com.kappzzang.jeongsan.data.ExpenseDetailState
import com.kappzzang.jeongsan.expensedetail.expensedetailpage.ExpenseDetailFragmentViewModel
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.ExpenseItemWithDetails
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.usecase.EditExpenseDetailUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseDetailUseCase
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
class ExpenseDetailViewModelTest {
    private val getExpenseDetailUseCase = mockk<GetExpenseDetailUseCase>(relaxed = true)
    private val editExpenseDetailUseCase = mockk<EditExpenseDetailUseCase>(relaxed = true)
    private lateinit var viewModel: ExpenseDetailFragmentViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        Dispatchers.setMain(testDispatcher)

        viewModel = ExpenseDetailFragmentViewModel(
            ioDispatcher = testDispatcher,
            getExpenseDetailUseCase = getExpenseDetailUseCase,
            editExpenseDetailUseCase = editExpenseDetailUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    private fun mockGetExpenseDetailUseCase() {
        coEvery { getExpenseDetailUseCase.invoke(any()) } returns Result.success(
            ExpenseItemWithDetails(
                item = ExpenseItem(
                    id = "1",
                    state = ExpenseState.NOT_CONFIRMED,
                    price = 100,
                    name = "name"
                ),
                expenseImageUrl = "url",
                expenseDetails = listOf(
                    ExpenseDetailItem("testId", "testItem", 100, 30, 20)
                )
            )
        )
    }

    @Test
    fun `아이템을 체크 해제 시 개수가 0으로 변경된다`() = runTest {
        // given
        mockGetExpenseDetailUseCase()

        viewModel.setInitialData("testId", "", true)

        // when
        advanceUntilIdle()
        viewModel.updateItemCheck(false, 0)
        advanceUntilIdle()

        // then
        assertThat(viewModel.expense.value.expenseDetails[0].selectedQuantity).isEqualTo(0)
    }

    @Test
    fun `아이템 개수 변경 시 내부 데이터에 반영된다`() = runTest {
        // given
        mockGetExpenseDetailUseCase()
        viewModel.setInitialData("testId", "", true)
        val testSelected = 10

        // when
        advanceUntilIdle()
        viewModel.updateSelectedQuantity(testSelected, 0)
        advanceUntilIdle()

        // then
        assertThat(viewModel.expense.value.expenseDetails[0].selectedQuantity).isEqualTo(
            testSelected
        )
    }

    @Test
    fun `초기화 함수 실행 후 뷰모델의 상태가 정상적으로 초기화된다`() = runTest {
        // given
        val firstDetailItem = ExpenseDetailItem("testId1", "testItem1", 1000, 10, 10)
        val secondDetailItem = ExpenseDetailItem("testId2", "testItem2", 1200, 20, 0)
        coEvery { getExpenseDetailUseCase(any()) } returns Result.success(
            ExpenseItemWithDetails(
                item = ExpenseItem.EMPTY,
                expenseImageUrl = "",
                expenseDetails = listOf(
                    firstDetailItem,
                    secondDetailItem
                )
            )
        )

        // when
        viewModel.setInitialData("", "", true)
        advanceUntilIdle()

        // then
        assertThat(viewModel.expense.value.expenseDetails[0]).isEqualTo(firstDetailItem)
        assertThat(viewModel.expense.value.expenseDetails[1]).isEqualTo(secondDetailItem)
    }

    @Test
    fun `저장 버튼을 누르면 아이템 상세 버튼을 저장하는 use case에 올바른 데이터가 담겨 호출된다`() = runTest {
        // given
        val expenseId = "1234"
        val groupId = "4321"
        val expenseDetails = listOf(
            ExpenseDetailItem("testId", "testItem", 100, 30, 20)
        )
        backgroundScope.launch(StandardTestDispatcher(testScheduler)) {
            viewModel.expenseDetailUIData.collect()
        }

        coEvery { getExpenseDetailUseCase.invoke(any()) } returns Result.success(
            ExpenseItemWithDetails(
                item = ExpenseItem(
                    id = expenseId,
                    state = ExpenseState.NOT_CONFIRMED,
                    price = 100,
                    name = "name"
                ),
                expenseImageUrl = "url",
                expenseDetails = expenseDetails
            )
        )

        coEvery { editExpenseDetailUseCase(any(), any(), any()) } returns Result.success(Unit)

        // when
        viewModel.setInitialData(expenseId, groupId, true)
        advanceUntilIdle()
        viewModel.saveExpenseDetail()
        advanceUntilIdle()

        // then
        coVerify { editExpenseDetailUseCase(expenseDetails, expenseId, groupId) }
    }

    @Test
    fun `저장 버튼을 눌러서 저장이 성공하면 ExpenseDetailState의 최종값 SUCCESS가 된다`() = runTest {
        // given
        mockGetExpenseDetailUseCase()
        coEvery { editExpenseDetailUseCase(any(), any(), any()) } returns Result.success(Unit)

        // when
        viewModel.setInitialData("11", "22", true)
        advanceUntilIdle()
        viewModel.saveExpenseDetail()
        advanceUntilIdle()

        // then
        assertThat(viewModel.expenseDetailSaveState.value).isEqualTo(ExpenseDetailState.Success)
    }

    @Test
    fun `저장 버튼을 눌러서 저장이 실패하면 ExpenseDetailState의 최종값 FAILED가 된다`() = runTest {
        // given
        mockGetExpenseDetailUseCase()
        coEvery {
            editExpenseDetailUseCase(
                any(),
                any(),
                any()
            )
        } returns Result.failure(Exception())

        // when
        viewModel.setInitialData("11", "22", true)
        advanceUntilIdle()
        viewModel.saveExpenseDetail()
        advanceUntilIdle()

        // then
        assertThat(
            viewModel.expenseDetailSaveState.value
        ).isInstanceOf(ExpenseDetailState.Failed::class.java)
    }
}
