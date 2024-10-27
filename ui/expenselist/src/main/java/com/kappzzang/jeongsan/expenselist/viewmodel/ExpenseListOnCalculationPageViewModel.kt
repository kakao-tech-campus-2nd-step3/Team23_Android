package com.kappzzang.jeongsan.expenselist.viewmodel

import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.usecase.GetExpenseListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@HiltViewModel
class ExpenseListOnCalculationPageViewModel @Inject constructor(
    getExpenseListUseCase: GetExpenseListUseCase
) : ExpenseListPageViewModel(getExpenseListUseCase) {
    override fun fetchDefaultList(groupId: String) {
        fetchExpenseList(ExpenseState.NOT_CONFIRMED, groupId)
    }

    // 미확인 + 확인 지출 모두 불러오기
    private fun fetchCalculatingExpenseList(groupId: String) {
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

    fun clickAllExpensesChipButton() {
        fetchCalculatingExpenseList(groupId.value)
    }

    fun clickOnlyNotConfirmedExpensesChipButton() {
        fetchExpenseList(ExpenseState.NOT_CONFIRMED, groupId.value)
    }

    fun clickOnlyConfirmedExpensesChipButton() {
        fetchExpenseList(ExpenseState.CONFIRMED, groupId.value)
    }
}
