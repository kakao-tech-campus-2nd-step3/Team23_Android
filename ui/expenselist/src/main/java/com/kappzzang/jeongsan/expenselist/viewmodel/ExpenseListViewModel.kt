package com.kappzzang.jeongsan.expenselist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.usecase.GetCurrentGroupInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val getCurrentGroupInfoUseCase: GetCurrentGroupInfoUseCase
) : ViewModel() {
    private var _groupId = MutableStateFlow("")
    private val _groupName = MutableStateFlow("")

    val groupName = _groupName.asStateFlow()

    val groupId = _groupId.asStateFlow()

    private val _selectedExpense = MutableStateFlow("")
    val selectedExpense = _selectedExpense.asStateFlow()

    private fun fetchGroupInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentGroupInfoUseCase(_groupId.value).map {
                it.name
            }.collect {
                _groupName.emit(it)
            }
        }
    }

    fun updateGroupId(groupId: String) {
        this._groupId.value = groupId

        fetchGroupInfo()
    }

    fun clickExpenseItem(expenseId: String) {
        _selectedExpense.value = expenseId
    }
}
