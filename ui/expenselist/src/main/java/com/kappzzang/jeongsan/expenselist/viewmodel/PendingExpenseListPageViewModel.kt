package com.kappzzang.jeongsan.expenselist.viewmodel

import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.usecase.ForceFetchExpenseListUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

@HiltViewModel
class PendingExpenseListPageViewModel @Inject constructor(
    getExpenseListUseCase: GetExpenseListUseCase,
    forceFetchExpenseListUseCase: ForceFetchExpenseListUseCase,
    ioDispatcher: CoroutineDispatcher
) : ExpenseListPageViewModel(getExpenseListUseCase, forceFetchExpenseListUseCase, ioDispatcher) {
    override fun fetchDefaultList(groupId: String) {
        fetchExpenseList(ExpenseState.TRANSFER_PENDING, groupId)
    }

    override fun refresh() {
        _refreshState.value = ExpenseListRefreshingState.REFRESHING
        viewModelScope.launch(ioDispatcher) {
            forceFetchExpenseList(ExpenseState.TRANSFER_PENDING, groupId.value)

            _refreshState.value = ExpenseListRefreshingState.FINISHED
        }
    }
}
