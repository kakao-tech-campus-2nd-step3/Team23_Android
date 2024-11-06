package com.kappzzang.jeongsan.expenselist.viewmodel

import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.usecase.GetExpenseListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

@HiltViewModel
class CompleteExpenseListPageViewModel @Inject constructor(
    getExpenseListUseCase: GetExpenseListUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ExpenseListPageViewModel(getExpenseListUseCase, ioDispatcher) {
    override fun fetchDefaultList(groupId: String) {
        fetchExpenseList(ExpenseState.TRANSFERED, groupId)
    }
}
