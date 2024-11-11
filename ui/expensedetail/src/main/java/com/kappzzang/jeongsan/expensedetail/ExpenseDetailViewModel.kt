package com.kappzzang.jeongsan.expensedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.usecase.RevertExpenseToOngoingUseCase
import com.kappzzang.jeongsan.usecase.SetExpenseToPendingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ExpenseDetailPage { EXPENSE_DETAIL, SELECTION_STATUS }

enum class ExpenseDetailState { IDLE, UPLOADING, SUCCESS, FAILED, SWITCHING_TO_PENDING }

internal fun ExpenseState.editable(): Boolean =
    this == ExpenseState.NOT_CONFIRMED || this == ExpenseState.CONFIRMED

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val setExpenseToPendingUseCase: SetExpenseToPendingUseCase,
    private val setExpenseToOngoingUseCase: RevertExpenseToOngoingUseCase
) : ViewModel() {
    private val _currentPage = MutableStateFlow(ExpenseDetailPage.EXPENSE_DETAIL)
    private val _showPayerUI = MutableStateFlow(false)

    private val _expenseDetailState = MutableStateFlow(ExpenseDetailState.IDLE)

    var groupId = ""
        private set
    var expenseId = ""
        private set
    private val _expenseState = MutableStateFlow(ExpenseState.TRANSFERED)
    val expenseState = _expenseState.asStateFlow()

    val currentPage = _currentPage.asStateFlow()
    val showPayerUI = _showPayerUI.asStateFlow()
    val expenseDetailState = _expenseDetailState.asStateFlow()

    private fun switchToPendingExpense() {
        _expenseDetailState.value = ExpenseDetailState.SWITCHING_TO_PENDING

        viewModelScope.launch(ioDispatcher) {
            setExpenseToPendingUseCase.invoke(expenseId, groupId)
                .onSuccess {
                    _expenseDetailState.value = ExpenseDetailState.SUCCESS
                }
                .onFailure {
                    _expenseDetailState.value = ExpenseDetailState.FAILED
                    it.printStackTrace()
                }
        }
    }

    private fun switchToOngoingExpense() {
        _expenseDetailState.value = ExpenseDetailState.SWITCHING_TO_PENDING

        viewModelScope.launch(ioDispatcher) {
            setExpenseToOngoingUseCase.invoke(expenseId, groupId)
                .onSuccess {
                    _expenseDetailState.value = ExpenseDetailState.SUCCESS
                }
                .onFailure {
                    _expenseDetailState.value = ExpenseDetailState.FAILED
                    it.printStackTrace()
                }
        }
    }

    fun setSaveResult(isSuccess: Boolean) {
        _expenseDetailState.value =
            if (isSuccess) ExpenseDetailState.SUCCESS else ExpenseDetailState.FAILED
    }

    fun setInitialData(
        expenseId: String,
        groupId: String,
        expenseState: ExpenseState,
        isPayer: Boolean
    ) {
        this.expenseId = expenseId
        this.groupId = groupId
        _expenseState.value = expenseState

        if (isPayer) {
            _showPayerUI.value = true
            _currentPage.value = ExpenseDetailPage.SELECTION_STATUS
        } else {
            _showPayerUI.value = false
            _currentPage.value = ExpenseDetailPage.EXPENSE_DETAIL
        }
    }

    private fun dismissAndClose() {
        _expenseDetailState.value = ExpenseDetailState.SUCCESS
    }

    private fun clickSaveDetailsAndClose() {
        _expenseDetailState.value = ExpenseDetailState.UPLOADING
    }

    private fun clickToSelectionStatus() {
        _currentPage.value = ExpenseDetailPage.SELECTION_STATUS
    }

    private fun clickToExpenseDetail() {
        _currentPage.value = ExpenseDetailPage.EXPENSE_DETAIL
    }

    private fun clickDetailPageLeftButton() {
        when (expenseState.value) {
            ExpenseState.CONFIRMED,ExpenseState.NOT_CONFIRMED -> clickSaveDetailsAndClose()
            ExpenseState.TRANSFER_PENDING -> dismissAndClose()
            ExpenseState.TRANSFERED -> dismissAndClose()
        }
    }

    private fun clickStatusPageLeftButton() {
        when (expenseState.value) {
            ExpenseState.CONFIRMED, ExpenseState.NOT_CONFIRMED -> switchToPendingExpense()
            ExpenseState.TRANSFER_PENDING -> switchToOngoingExpense()
            ExpenseState.TRANSFERED -> dismissAndClose()
        }
    }

    private fun clickDetailPageRightButton() {
        clickToSelectionStatus()
    }

    private fun clickStatusPageRightButton() {
        clickToExpenseDetail()
    }

    fun clickLeftButton() {
        if (currentPage.value == ExpenseDetailPage.EXPENSE_DETAIL) {
            clickDetailPageLeftButton()
        } else {
            clickStatusPageLeftButton()
        }
    }

    fun clickRightButton() {
        if (currentPage.value == ExpenseDetailPage.EXPENSE_DETAIL) {
            clickDetailPageRightButton()
        } else {
            clickStatusPageRightButton()
        }
    }
}
