package com.kappzzang.jeongsan.expenselist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.data.ExpenseListUIState
import com.kappzzang.jeongsan.data.HasGroupId
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.usecase.CompleteGroupUseCase
import com.kappzzang.jeongsan.usecase.GetCurrentGroupInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val getCurrentGroupInfoUseCase: GetCurrentGroupInfoUseCase,
    private val completeGroupUseCase: CompleteGroupUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<ExpenseListUIState>(ExpenseListUIState.Initial)
    val uiState = _uiState.asStateFlow()

    private suspend fun fetchGroupInfo(groupId: String) {
        getCurrentGroupInfoUseCase(groupId).collect {
            _uiState.emit(
                ExpenseListUIState.Idle(
                    groupId = groupId,
                    groupName = it.name,
                    groupSubject = it.subject
                )
            )
        }
    }

    fun updateGroupId(groupId: String) {
        if (uiState.value is ExpenseListUIState.Initial ||
            (uiState.value as? HasGroupId)?.groupId.isNullOrEmpty()
        ) {
            _uiState.value = ExpenseListUIState.FetchingGroupUIItem(groupId)

            viewModelScope.launch(ioDispatcher) {
                fetchGroupInfo(groupId)
            }
        }
    }

    fun clickExpenseItem(expenseId: String, state: ExpenseState, isPayer: Boolean) {
        (this.uiState.value as? ExpenseListUIState.Idle)?.let {
            this._uiState.value = ExpenseListUIState.SelectingExpense(
                groupId = it.groupId,
                groupName = it.groupName,
                groupSubject = it.groupSubject,
                selectedExpenseId = expenseId,
                expenseState = state,
                isPayer = isPayer
            )
        }
    }

    fun resetExpenseSelection() {
        (this.uiState.value as? ExpenseListUIState.SelectingExpense)?.let {
            this._uiState.value = ExpenseListUIState.Idle(
                groupId = it.groupId,
                groupName = it.groupName,
                groupSubject = it.groupSubject
            )
        }
    }

    fun completeGroup() {
        (this.uiState.value as? ExpenseListUIState.Idle)?.let {
            viewModelScope.launch(ioDispatcher) {
                completeGroupUseCase(it.groupId).onSuccess { _ ->
                    _uiState.emit(ExpenseListUIState.CompleteSuccess(it.groupId, it.groupSubject))
                }.onFailure { e ->
                    _uiState.emit(ExpenseListUIState.CompleteFailed(e.message ?: ""))
                }
            }
            this._uiState.value = ExpenseListUIState.Completing(
                groupId = it.groupId,
                groupName = it.groupName,
                groupSubject = it.groupSubject
            )
        }
    }
}
