package com.kappzzang.jeongsan.expensedetail

import androidx.lifecycle.ViewModel
import com.kappzzang.jeongsan.expensedetail.expensedetailpage.ExpenseDetailSaveState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

enum class ExpenseDetailPage { EXPENSE_DETAIL, SELECTION_STATUS }

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _currentPage = MutableStateFlow(ExpenseDetailPage.EXPENSE_DETAIL)
    private val _showPayerUI = MutableStateFlow(false)

    private val _expenseDetailSaveState = MutableStateFlow(ExpenseDetailSaveState.NOT_UPLOADING)

    var groupId = ""
        private set
    var expenseId = ""
        private set
    var editable = false
        private set

    val currentPage = _currentPage.asStateFlow()
    val showPayerUI = _showPayerUI.asStateFlow()
    val expenseDetailSaveState = _expenseDetailSaveState.asStateFlow()

    fun setSaveResult(isSuccess: Boolean) {
        _expenseDetailSaveState.value =
            if (isSuccess) ExpenseDetailSaveState.SUCCESS else ExpenseDetailSaveState.FAILED
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
        _expenseDetailSaveState.value = ExpenseDetailSaveState.UPLOADING
    }

    fun clickToSelectionStatus() {
        _currentPage.value = ExpenseDetailPage.SELECTION_STATUS
    }

    fun clickToExpenseDetail() {
        _currentPage.value = ExpenseDetailPage.EXPENSE_DETAIL
    }

    fun clickSwitchToPending() {

    }
}
