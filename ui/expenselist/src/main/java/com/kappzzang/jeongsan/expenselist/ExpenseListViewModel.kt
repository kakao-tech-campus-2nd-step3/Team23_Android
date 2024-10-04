package com.kappzzang.jeongsan.expenselist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.data.ExpenseListViewUIData
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.usecase.GetCurrentGroupInfoUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseListUseCase
import com.kappzzang.jeongsan.util.IntegerFormatter.formatDecimalSeparator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val getCurrentGroupInfoUseCase: GetCurrentGroupInfoUseCase,
    private val getExpenseListUseCase: GetExpenseListUseCase
) : ViewModel() {

    private var expenseListFetchingJob: Job? = null
    private var groupId: String = ""

    private val expenseList =
        MutableStateFlow(ExpenseListResponse.emptyList())
    private val groupName = MutableStateFlow("")

    private val _selectedExpense = MutableStateFlow("")

    private val _uiData by lazy {
        combine(
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
    }

    val uiData by lazy {
        _uiData
    }

    val selectedExpense = _selectedExpense.asStateFlow()

    private fun cancelPreviousJob() {
        if (expenseListFetchingJob?.isCompleted != false) {
            return
        }
        expenseListFetchingJob?.cancel()
    }

    private fun fetchExpenseList(expenseState: ExpenseState) {
        cancelPreviousJob()
        expenseListFetchingJob = viewModelScope.launch(Dispatchers.IO) {
            getExpenseListUseCase(groupId, expenseState)
                .collect {
                    expenseList.emit(it)
                }
        }
    }

    // 미확인 + 확인 지출 모두 불러오기
    private fun fetchCalculatingExpenseList() {
        cancelPreviousJob()
        expenseListFetchingJob = viewModelScope.launch(Dispatchers.IO) {
            getExpenseListUseCase(groupId, ExpenseState.CONFIRMED).zip(
                getExpenseListUseCase(
                    groupId,
                    ExpenseState.NOT_CONFIRMED
                )
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

    private fun fetchGroupInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentGroupInfoUseCase(groupId).map {
                it.name
            }.collect {
                groupName.emit(it)
            }
        }
    }

    fun clickPendSendingMenuButton() {
        fetchExpenseList(ExpenseState.TRANSFER_PENDING)
    }

    fun clickOnCalculatingMenuButton() {
        fetchExpenseList(ExpenseState.NOT_CONFIRMED)
    }

    fun clickSentCompleteMenuButton() {
        fetchExpenseList(ExpenseState.TRANSFERED)
    }

    fun clickAllExpensesChipButton() {
        fetchCalculatingExpenseList()
    }

    fun clickOnlyNotConfirmedExpensesChipButton() {
        fetchExpenseList(ExpenseState.NOT_CONFIRMED)
    }

    fun clickOnlyConfirmedExpensesChipButton() {
        fetchExpenseList(ExpenseState.CONFIRMED)
    }

    fun updateGroupId(groupId: String) {
        this.groupId = groupId

        fetchGroupInfo()
    }

    fun clickExpenseItem(expenseId: String) {
        _selectedExpense.value = expenseId
    }
}
