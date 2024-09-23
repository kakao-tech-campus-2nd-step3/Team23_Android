package com.kappzzang.jeongsan.ui.expenselist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.domain.model.ExpenseListResponse
import com.kappzzang.jeongsan.domain.model.ExpenseState
import com.kappzzang.jeongsan.domain.usecase.GetCurrentGroupInfoUseCase
import com.kappzzang.jeongsan.domain.usecase.GetExpenseListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

fun Int.formatDecimalSeparator(): String = toString()
    .reversed()
    .chunked(3)
    .joinToString(",")
    .reversed()

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val getCurrentGroupInfoUseCase: GetCurrentGroupInfoUseCase,
    private val getExpenseListUseCase: GetExpenseListUseCase
) : ViewModel() {

    private val expenseList = MutableStateFlow(ExpenseListResponse.emptyList())
    private val groupName = getCurrentGroupInfoUseCase("").map {
        it.name
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        ""
    )

    private val _uiData = combine(
        expenseList,
        groupName
    ) { expenseList, groupName ->
        val totalPrice = expenseList.totalPrice
        val priceToSend = expenseList.totalExpenseToSend
        val items = expenseList.expenseList

        ExpenseListViewUIData(
            totalPriceText = "${totalPrice.formatDecimalSeparator()}원",
            priceToSendText = "${priceToSend.formatDecimalSeparator()}원",
            groupNameText = groupName,
            expenseItems = items
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        ExpenseListViewUIData("", "", "", listOf())
    )

    private var expenseListFetchingJob: Job? = null
    val uiData = _uiData

    private fun cancelPreviousJob() {
        if (expenseListFetchingJob?.isCompleted != false) {
            return
        }
        expenseListFetchingJob?.cancel()
    }

    private fun getCurrentGroupInfo(expenseState: ExpenseState) {
        viewModelScope.launch {
            getExpenseListUseCase("", expenseState).collect {
            }
        }
    }

    private fun getExpenseList(expenseState: ExpenseState) {
        cancelPreviousJob()
        expenseListFetchingJob = viewModelScope.launch {
            getExpenseListUseCase("", expenseState)
                .collect {
                    expenseList.emit(it)
                }
        }
    }

    // 미확인 + 확인 지출 모두 불러오기
    private fun getAllCalculatingExpenseList() {
        cancelPreviousJob()
        expenseListFetchingJob = viewModelScope.launch {
            getExpenseListUseCase("", ExpenseState.CONFIRMED).zip(
                getExpenseListUseCase("", ExpenseState.NOT_CONFIRMED)
            ) { confirmed, notConfirmed ->
                ExpenseListResponse(
                    expenseList = confirmed.expenseList.toMutableList() + notConfirmed.expenseList,
                    totalPrice = confirmed.totalPrice + notConfirmed.totalPrice,
                    totalExpenseToSend = 0
                )
            }.collect {
                expenseList.emit(it)
            }
        }
    }

    fun clickPendSendingMenuButton() {
        getExpenseList(ExpenseState.TRANSFER_PENDING)
        Log.d("KSC", "clicked Pending")
    }

    fun clickOnCalculatingMenuButton() {
        getExpenseList(ExpenseState.NOT_CONFIRMED)
    }

    fun clickSentCompleteMenuButton() {
        getExpenseList(ExpenseState.TRANSFERED)
    }

    fun clickAllExpensesChipButton() {
        getAllCalculatingExpenseList()
    }

    fun clickOnlyNotConfirmedExpensesChipButton() {
        getExpenseList(ExpenseState.NOT_CONFIRMED)
    }

    fun clickOnlyConfirmedExpensesChipButton() {
        getExpenseList(ExpenseState.CONFIRMED)
    }
}
