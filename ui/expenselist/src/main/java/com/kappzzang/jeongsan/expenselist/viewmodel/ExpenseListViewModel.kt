package com.kappzzang.jeongsan.expenselist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.data.ExpenseListGroupInfoUIData
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.usecase.CompleteGroupUseCase
import com.kappzzang.jeongsan.usecase.GetCurrentGroupInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SelectedExpenseData(val expenseId: String, val editable: Boolean)

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val getCurrentGroupInfoUseCase: GetCurrentGroupInfoUseCase,
    private val completeGroupUseCase: CompleteGroupUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _groupId = MutableStateFlow("")
    private val _groupUIItem = MutableStateFlow(ExpenseListGroupInfoUIData.EMPTY)

    val groupUIItem = _groupUIItem.asStateFlow()

    val groupId = _groupId.asStateFlow()

    private val _selectedExpense = MutableStateFlow(SelectedExpenseData("", false))
    val selectedExpense = _selectedExpense.asStateFlow()

    private val _completeGroupState = MutableStateFlow(CompleteGroupState.IDLE)
    val completeGroupState = _completeGroupState.asStateFlow()

    private fun fetchGroupInfo() {
        viewModelScope.launch(ioDispatcher) {
            getCurrentGroupInfoUseCase(_groupId.value).collect {
                _groupUIItem.emit(
                    ExpenseListGroupInfoUIData(
                        groupName = it.name,
                        groupSubject = it.subject
                    )
                )
            }
        }
    }

    fun updateGroupId(groupId: String) {
        this._groupId.value = groupId

        fetchGroupInfo()
    }

    fun clickExpenseItem(expenseId: String, state: ExpenseState) {
        _selectedExpense.value =
            SelectedExpenseData(
                expenseId,
                (state == ExpenseState.NOT_CONFIRMED) || (state == ExpenseState.CONFIRMED)
            )
    }

    fun resetExpenseSelection() {
        _selectedExpense.value = SelectedExpenseData("", false)
    }

    fun completeGroup() {
        viewModelScope.launch(ioDispatcher) {
            completeGroupUseCase(_groupId.value).onSuccess {
                _completeGroupState.value = CompleteGroupState.SUCCESS
            }.onFailure {
                _completeGroupState.value = CompleteGroupState.FAILED
            }
        }
    }
}

enum class CompleteGroupState { IDLE, SUCCESS, FAILED }
