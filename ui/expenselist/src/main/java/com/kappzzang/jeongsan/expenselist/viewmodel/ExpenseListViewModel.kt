package com.kappzzang.jeongsan.expenselist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.data.ExpenseListViewUIData
import com.kappzzang.jeongsan.data.ExpenseUiItem
import com.kappzzang.jeongsan.expenselist.util.ExpenseUiItemMapper.mapToExpenseUiItem
import com.kappzzang.jeongsan.expenselist.util.ExpenseUiItemMapper.sortItemsByTime
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.usecase.GetCurrentGroupInfoUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseListUseCase
import com.kappzzang.jeongsan.util.IntegerFormatter.formatDecimalSeparator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

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
