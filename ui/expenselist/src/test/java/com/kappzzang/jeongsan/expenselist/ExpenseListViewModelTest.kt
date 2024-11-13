package com.kappzzang.jeongsan.expenselist

import android.util.Log
import com.kappzzang.jeongsan.data.ExpenseListUIState
import com.kappzzang.jeongsan.expenselist.viewmodel.ExpenseListViewModel
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.model.GroupItem
import com.kappzzang.jeongsan.usecase.CompleteGroupUseCase
import com.kappzzang.jeongsan.usecase.GetCurrentGroupInfoUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
class ExpenseListViewModelTest {
    private val getCurrentGroupInfoUseCase = mockk<GetCurrentGroupInfoUseCase>(relaxed = true)
    private val completeGroupUseCase = mockk<CompleteGroupUseCase>(relaxed = true)
    private lateinit var viewModel: ExpenseListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        Dispatchers.setMain(testDispatcher)

        viewModel = ExpenseListViewModel(
            ioDispatcher = testDispatcher,
            getCurrentGroupInfoUseCase = getCurrentGroupInfoUseCase,
            completeGroupUseCase = completeGroupUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    private fun getTestGroupItem(id: String = "1"): GroupItem =
        GroupItem(
            id = id,
            name = "name",
            subject = "subject",
            isCompleted = false,
            profileImageURL = emptyList()
        )

    @Test
    fun `처음 UI State는 Initial 이다`() {
        assertThat(viewModel.uiState.value).isEqualTo(ExpenseListUIState.Initial)
    }

    @Test
    fun `그룹 ID를 업데이트 한 결과 UI State의 최종값은 Idle이 된다`() = runTest {
        // given
        every { getCurrentGroupInfoUseCase(any()) } returns flow { emit(getTestGroupItem()) }

        // when
        viewModel.updateGroupId("0")
        advanceUntilIdle()

        // then
        assertThat(viewModel.uiState.value).isInstanceOf(ExpenseListUIState.Idle::class.java)
    }

    @Test
    fun `그룹 ID를 업데이트 하면 getCurrentGroupInfoUseCase를 호출한다`() = runTest {
        // given
        val testGroupId = "123"
        every { getCurrentGroupInfoUseCase(any()) } returns flow { emit(getTestGroupItem()) }

        // when
        viewModel.updateGroupId(testGroupId)
        advanceUntilIdle()

        // then
        verify { getCurrentGroupInfoUseCase(testGroupId) }
    }

    @Test
    fun `그룹 ID를 업데이트 하면 UI State에 그룹 정보가 정상적으로 포함된다`() = runTest {
        // given
        val testId = "10"
        val testName = "name"
        val testSubject = "subject"
        every { getCurrentGroupInfoUseCase(any()) } returns flow {
            emit(
                GroupItem(
                    id = testId,
                    name = testName,
                    subject = testSubject,
                    isCompleted = false,
                    profileImageURL = emptyList()
                )
            )
        }

        // when
        viewModel.updateGroupId(testId)
        advanceUntilIdle()

        // then
        assertThat(viewModel.uiState.value).isEqualTo(
            ExpenseListUIState.Idle(
                groupName = testName,
                groupId = testId,
                groupSubject = testSubject
            )
        )
    }

    @Test
    fun `지출 아이템을 클릭하면 UI State의 최종값은 SelectingExpense가 된다`() = runTest {
        // given
        val testExpenseId = "expense0"
        val testExpenseState = ExpenseState.CONFIRMED
        val testIsPayer = true

        every { getCurrentGroupInfoUseCase(any()) } returns flow { emit(getTestGroupItem()) }

        // when
        viewModel.updateGroupId("0")
        advanceUntilIdle()

        viewModel.clickExpenseItem(
            expenseId = testExpenseId,
            state = testExpenseState,
            isPayer = testIsPayer
        )
        advanceUntilIdle()

        // then
        assertThat(viewModel.uiState.value)
            .isInstanceOf(ExpenseListUIState.SelectingExpense::class.java)
    }

    @Test
    fun `지출 아이템을 클릭하면 선택한 정보가 UI State에 포함된다`() = runTest {
        // given
        val testExpenseId = "expense0"
        val testExpenseState = ExpenseState.CONFIRMED
        val testIsPayer = true

        every { getCurrentGroupInfoUseCase(any()) } returns flow { emit(getTestGroupItem()) }

        // when
        viewModel.updateGroupId("0")
        advanceUntilIdle()

        viewModel.clickExpenseItem(
            expenseId = testExpenseId,
            state = testExpenseState,
            isPayer = testIsPayer
        )
        advanceUntilIdle()

        // then
        assertThat(viewModel.uiState.value)
            .isInstanceOf(ExpenseListUIState.SelectingExpense::class.java)
        (viewModel.uiState.value as? ExpenseListUIState.SelectingExpense)?.let {
            assertThat(it.selectedExpenseId).isEqualTo(testExpenseId)
            assertThat(it.expenseState).isEqualTo(testExpenseState)
            assertThat(it.isPayer).isEqualTo(testIsPayer)
        }
    }

    @Test
    fun `모임 종료를 하면 completeGroupUseCase를 호출한다`() = runTest {
        // given
        val testGroupId = "groupId0"

        every { getCurrentGroupInfoUseCase(any()) } returns flow { emit(getTestGroupItem()) }
        coEvery { completeGroupUseCase(any()) } returns Result.success(true)

        // when
        viewModel.updateGroupId(testGroupId)
        advanceUntilIdle()

        viewModel.completeGroup()
        advanceUntilIdle()

        // then
        coVerify { completeGroupUseCase(testGroupId) }
    }

    @Test
    fun `모임 종료 요청에 실패하면 UI State의 최종값은 오류 메세지를 포함한 CompleteFailed 이다`() = runTest {
        // given
        val testErrorMessage = "test error"
        every { getCurrentGroupInfoUseCase(any()) } returns flow { emit(getTestGroupItem()) }
        coEvery { completeGroupUseCase(any()) } returns Result.failure(Exception(testErrorMessage))

        // when
        viewModel.updateGroupId("1")
        advanceUntilIdle()

        viewModel.completeGroup()
        advanceUntilIdle()

        // then
        assertThat(viewModel.uiState.value)
            .isEqualTo(ExpenseListUIState.CompleteFailed(testErrorMessage))
    }

    @Test
    fun `모임 종료 요청에 성공하면 UI State의 최종값은 그룹 정보가 포함된 CompleteSuccess 이다`() = runTest {
        // given
        val testGroupName = "groupName"
        val testGroupSubject = "groupSubject"
        val testGroupItem = getTestGroupItem().copy(
            name = testGroupName,
            subject = testGroupSubject
        )
        every { getCurrentGroupInfoUseCase(any()) } returns flow { emit(testGroupItem) }
        coEvery { completeGroupUseCase(any()) } returns Result.success(true)

        // when
        viewModel.updateGroupId("1")
        advanceUntilIdle()

        viewModel.completeGroup()
        advanceUntilIdle()

        // then
        assertThat(viewModel.uiState.value)
            .isEqualTo(
                ExpenseListUIState.CompleteSuccess(
                    groupName = testGroupName,
                    groupSubject = testGroupSubject
                )
            )
    }
}
