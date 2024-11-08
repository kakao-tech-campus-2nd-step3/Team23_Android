package com.kappzzang.jeongsan.expensedetail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ExpenseDetailPage { EXPENSE_DETAIL, SELECTION_STATUS }

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _currentPage = MutableStateFlow(ExpenseDetailPage.EXPENSE_DETAIL)
    private val _showPayerUI = MutableStateFlow(false)

    var groupId = ""
        private set
    var expenseId = ""
        private set
    var editable = false
        private set

    val currentPage = _currentPage.asStateFlow()
    val showPayerUI = _showPayerUI.asStateFlow()

    fun setInitialData(expenseId: String, groupId: String, editable: Boolean, isPayer: Boolean) {
        this.expenseId = expenseId
        this.groupId = groupId
        this.editable = editable

        if(isPayer) {
            _showPayerUI.value = true
            _currentPage.value = ExpenseDetailPage.SELECTION_STATUS
        }
        else {
            _showPayerUI.value = false
            _currentPage.value = ExpenseDetailPage.EXPENSE_DETAIL
        }
    }

    private fun saveDetailsAndClose(){

    }

    private fun toSelectionStatus(){
        _currentPage.value = ExpenseDetailPage.SELECTION_STATUS
    }

    private fun toExpenseDetail(){
        _currentPage.value = ExpenseDetailPage.EXPENSE_DETAIL
    }

    private fun switchToPending() {

    }

    fun clickPrimaryButton() {
        if(currentPage.value == ExpenseDetailPage.EXPENSE_DETAIL){
            saveDetailsAndClose()
        }
        else{
            switchToPending()
        }
    }

    fun clickSecondaryButton() {
        if(currentPage.value == ExpenseDetailPage.EXPENSE_DETAIL) {
            toSelectionStatus()
        }
        else{
            toExpenseDetail()
        }
    }
}
