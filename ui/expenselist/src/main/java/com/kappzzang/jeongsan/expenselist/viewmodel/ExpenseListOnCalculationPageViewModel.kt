package com.kappzzang.jeongsan.expenselist.viewmodel

import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.usecase.ForceFetchExpenseListUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

enum class ChipSelectionState { ALL, NOT_CONFIRMED, CONFIRMED }

@HiltViewModel
class ExpenseListOnCalculationPageViewModel @Inject constructor(
    getExpenseListUseCase: GetExpenseListUseCase,
    forceFetchExpenseListUseCase: ForceFetchExpenseListUseCase,
    ioDispatcher: CoroutineDispatcher
) : ExpenseListPageViewModel(getExpenseListUseCase, forceFetchExpenseListUseCase, ioDispatcher) {
    private val _chipSelectionState = MutableStateFlow(ChipSelectionState.NOT_CONFIRMED)
    val chipSelectionState = _chipSelectionState.asStateFlow()

    override fun fetchDefaultList(groupId: String) {
        when (chipSelectionState.value) {
            ChipSelectionState.ALL -> fetchCalculatingExpenseList(groupId)
            ChipSelectionState.NOT_CONFIRMED -> fetchExpenseList(
                ExpenseState.NOT_CONFIRMED,
                groupId
            )
            ChipSelectionState.CONFIRMED -> fetchExpenseList(ExpenseState.CONFIRMED, groupId)
        }
    }

    override fun refresh() {
        _refreshState.value = ExpenseListRefreshingState.REFRESHING
        viewModelScope.launch(ioDispatcher) {
            when (_chipSelectionState.value) {
                ChipSelectionState.ALL -> {
                    forceFetchCalculatingExpenseList(groupId.value)
                }
                ChipSelectionState.NOT_CONFIRMED -> {
                    forceFetchExpenseList(ExpenseState.NOT_CONFIRMED, groupId.value)
                }

                ChipSelectionState.CONFIRMED -> {
                    forceFetchExpenseList(ExpenseState.CONFIRMED, groupId.value)
                }
            }

            _refreshState.value = ExpenseListRefreshingState.FINISHED
        }
    }

    // 미확인 + 확인 지출 모두 불러오기
    private fun fetchCalculatingExpenseList(groupId: String) {
        cancelPreviousJob()
        expenseListFetchingJob = viewModelScope.launch(ioDispatcher) {
            getExpenseListUseCase(groupId, ExpenseState.CONFIRMED).zip(
                getExpenseListUseCase(
                    groupId,
                    ExpenseState.NOT_CONFIRMED
                )
            ) { confirmed, notConfirmed ->
                zipExpenseListResponse(confirmed, notConfirmed)
            }.collect { result ->
                result.onSuccess {
                    expenseList.emit(it)
                }.onFailure {
                    expenseList.emit(ExpenseListResponse.emptyList())
                    handleExpenseListException(it)
                }
            }
        }
    }

    private suspend fun forceFetchCalculatingExpenseList(groupId: String) {
        if (expenseListFetchingJob?.isCompleted == false) {
            return
        }

        val notConfirmed = viewModelScope.run {
            forceFetchExpenseListUseCase(groupId, ExpenseState.NOT_CONFIRMED)
        }
        val confirmed = viewModelScope.run {
            forceFetchExpenseListUseCase(groupId, ExpenseState.CONFIRMED)
        }
        val result = zipExpenseListResponse(confirmed, notConfirmed)
        result.onSuccess {
            expenseList.emit(it)
        }.onFailure {
            expenseList.emit(ExpenseListResponse.emptyList())
            handleExpenseListException(it)
        }
    }

    private fun zipExpenseListResponse(
        first: Result<ExpenseListResponse>,
        second: Result<ExpenseListResponse>
    ): Result<ExpenseListResponse> {
        val firstSuccess = first.getOrElse {
            return Result.failure(it)
        }
        val secondSuccess = second.getOrElse {
            return Result.failure(it)
        }

        return Result.success(
            ExpenseListResponse(
                expenseList = firstSuccess.expenseList.toMutableList() + secondSuccess.expenseList,
                totalPrice = firstSuccess.totalPrice,
                totalExpenseToSend = 0
            )
        )
    }

    fun clickAllExpensesChipButton() {
        fetchCalculatingExpenseList(groupId.value)
        _chipSelectionState.value = ChipSelectionState.ALL
    }

    fun clickOnlyNotConfirmedExpensesChipButton() {
        fetchExpenseList(ExpenseState.NOT_CONFIRMED, groupId.value)
        _chipSelectionState.value = ChipSelectionState.NOT_CONFIRMED
    }

    fun clickOnlyConfirmedExpensesChipButton() {
        fetchExpenseList(ExpenseState.CONFIRMED, groupId.value)
        _chipSelectionState.value = ChipSelectionState.CONFIRMED
    }
}
