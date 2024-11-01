package com.kappzzang.jeongsan.expenselist.viewmodel

import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.usecase.GetExpenseListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompleteExpenseListPageViewModel @Inject constructor(
    getExpenseListUseCase: GetExpenseListUseCase
) : ExpenseListPageViewModel(getExpenseListUseCase) {
    override fun fetchDefaultList(groupId: String) {
        fetchExpenseList(ExpenseState.TRANSFERED, groupId)
    }
}
