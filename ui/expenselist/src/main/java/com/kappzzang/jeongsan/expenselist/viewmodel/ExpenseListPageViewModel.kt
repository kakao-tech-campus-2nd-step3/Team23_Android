package com.kappzzang.jeongsan.expenselist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.data.ExpenseListViewUIData
import com.kappzzang.jeongsan.expenselist.util.ExpenseUiItemMapper.mapToExpenseUiItem
import com.kappzzang.jeongsan.expenselist.util.ExpenseUiItemMapper.sortItemsByTime
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.usecase.GetExpenseListUseCase
import com.kappzzang.jeongsan.util.IntegerFormatter.formatDecimalSeparator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
open class ExpenseListPageViewModel @Inject constructor(
    protected val getExpenseListUseCase: GetExpenseListUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    protected var expenseListFetchingJob: Job? = null
    protected val expenseList =
        MutableStateFlow(ExpenseListResponse.emptyList())
    protected val groupId = MutableStateFlow("")

    private val _uiData by lazy {
        expenseList.map { expenseList ->
            val totalPrice = expenseList.totalPrice
            val priceToSend = expenseList.totalExpenseToSend
            val items = expenseList.expenseList

            ExpenseListViewUIData(
                totalPriceText = "${totalPrice.formatDecimalSeparator()}$CURRENCY_POSTFIX",
                priceToSendText = "${priceToSend.formatDecimalSeparator()}$CURRENCY_POSTFIX",
                expenseItems = mapToExpenseUiItem(sortItemsByTime(items))
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ExpenseListViewUIData.emptyData
        )
    }

    val uiData by lazy {
        _uiData
    }

    protected fun cancelPreviousJob() {
        if (expenseListFetchingJob?.isCompleted != false) {
            return
        }
        expenseListFetchingJob?.cancel()
    }

    protected fun fetchExpenseList(expenseState: ExpenseState, groupId: String) {
        cancelPreviousJob()
        expenseListFetchingJob = viewModelScope.launch(ioDispatcher) {
            getExpenseListUseCase(groupId, expenseState)
                .collect { result ->
                    result.onSuccess {
                        expenseList.emit(it)
                    }
                        .onFailure {
                            // TODO: ExpenseList 조회 실패 시 예외 처리
                        }
                }
        }
    }

    protected open fun fetchDefaultList(groupId: String) {
    }

    fun onFragmentStart(groupId: String) {
        if (this.groupId.value != groupId) {
            this.groupId.value = groupId
            fetchDefaultList(this.groupId.value)
        } else {
            return
        }
    }

    companion object {
        const val CURRENCY_POSTFIX = "원"
    }
}
