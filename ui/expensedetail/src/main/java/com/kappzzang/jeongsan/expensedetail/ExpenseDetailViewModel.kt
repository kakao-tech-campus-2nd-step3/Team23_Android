package com.kappzzang.jeongsan.expensedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.usecase.SetExpenseToPendingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ExpenseDetailPage { EXPENSE_DETAIL, SELECTION_STATUS }

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val setExpenseToPendingUseCase: SetExpenseToPendingUseCase
) : ViewModel() {
    private val _currentPage = MutableStateFlow(ExpenseDetailPage.EXPENSE_DETAIL)
    private val _showPayerUI = MutableStateFlow(false)

    private val _expenseDetailState = MutableStateFlow(ExpenseDetailState.IDLE)

    var groupId = ""
        private set
    var expenseId = ""
        private set
    var editable = false
        private set

    val currentPage = _currentPage.asStateFlow()
    val showPayerUI = _showPayerUI.asStateFlow()
    val expenseDetailState = _expenseDetailState.asStateFlow()

    private fun switchToPendingExpense() {
        if (_expenseDetailState.value != ExpenseDetailState.IDLE) {
            return
        }

        _expenseDetailState.value = ExpenseDetailState.SWITCHING_TO_PENDING
        viewModelScope.launch(ioDispatcher) {
            setExpenseToPendingUseCase.invoke(expenseId)
                .onSuccess {
                    _expenseDetailState.value = ExpenseDetailState.SUCCESS
                }
                .onFailure {
                    _expenseDetailState.value = ExpenseDetailState.FAILED
                }
        }
    }

    fun setSaveResult(isSuccess: Boolean) {
        _expenseDetailState.value =
            if (isSuccess) ExpenseDetailState.SUCCESS else ExpenseDetailState.FAILED
    }

    fun setInitialData(expenseId: String, groupId: String, editable: Boolean, isPayer: Boolean) {
        this.expenseId = expenseId
        this.groupId = groupId
        this.editable = editable

        if (isPayer) {
            _showPayerUI.value = true
            _currentPage.value = ExpenseDetailPage.SELECTION_STATUS
        } else {
            _showPayerUI.value = false
            _currentPage.value = ExpenseDetailPage.EXPENSE_DETAIL
        }
    }

    fun clickSaveDetailsAndClose() {
        _expenseDetailState.value = ExpenseDetailState.UPLOADING
    }

    fun clickToSelectionStatus() {
        _currentPage.value = ExpenseDetailPage.SELECTION_STATUS
    }

    fun clickToExpenseDetail() {
        _currentPage.value = ExpenseDetailPage.EXPENSE_DETAIL
    }

    fun clickSwitchToPending() {
        switchToPendingExpense()
    }
}

enum class ExpenseDetailState { IDLE, UPLOADING, SUCCESS, FAILED, SWITCHING_TO_PENDING }
