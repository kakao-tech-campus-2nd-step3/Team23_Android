package com.kappzzang.jeongsan.expenselist.viewmodel

import androidx.lifecycle.ViewModel
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.usecase.GetExpenseListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PendingExpenseListPageViewModel @Inject constructor(
    getExpenseListUseCase: GetExpenseListUseCase
) : ExpenseListPageViewModel(getExpenseListUseCase) {
    override fun fetchDefaultList(groupId: String) {
        fetchExpenseList(ExpenseState.TRANSFER_PENDING, groupId)
    }
}
