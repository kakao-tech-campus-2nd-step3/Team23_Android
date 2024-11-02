package com.kappzzang.jeongsan.expensedetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItemWithDetails
import com.kappzzang.jeongsan.usecase.EditExpenseDetailUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val getExpenseDetailUseCase: GetExpenseDetailUseCase,
    private val editExpenseDetailUseCase: EditExpenseDetailUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _expense = MutableStateFlow(ExpenseItemWithDetails.EMPTY)
    private val _expenseDetailList = MutableStateFlow(emptyList<ExpenseDetailItem>())
    val expenseDetailList = _expenseDetailList.asStateFlow()

    private val expenseId = MutableStateFlow("")
    val expense: StateFlow<ExpenseItemWithDetails> = _expense.asStateFlow()

    fun saveExpenseDetail() {
        viewModelScope.launch(ioDispatcher) {
            editExpenseDetailUseCase.invoke(expenseDetailList.value, expenseId.value)
            editExpenseDetailUseCase.invoke(
                expenseDetailList.value,
                expenseId.value,
                groupId = groupId.value
            )
        }
    }

    fun updateExpenseIdAndInit(id: String) {
        expenseId.value = id
        initExpense()
    }

    fun setInitialData(expenseId: String, groupId: String) {
        this.expenseId.value = expenseId
        this.groupId.value = groupId
        initExpense()
    }

    private fun initExpense() {
        viewModelScope.launch(ioDispatcher) {
            val result = getExpenseDetailUseCase.invoke(expenseId.value)
            result.onSuccess {
                _expense.value = it
                _expenseDetailList.emit(_expense.value.expenseDetails)
            }
                .onFailure {
                    // TODO: Expense Detail 조회 실패 시 예외처리
                }
        }
    }

    private fun getItemWithEnabled(item: ExpenseDetailItem, enabled: Boolean): ExpenseDetailItem =
        if (enabled) {
            item.copy(selectedQuantity = 1)
        } else {
            item.copy(selectedQuantity = 0)
        }
    }

    private fun getItemWithQuantity(item: ExpenseDetailItem, quantity: Int): ExpenseDetailItem =
        ExpenseDetailItem(
            id = item.id,
            itemName = item.itemName,
            itemQuantity = item.itemQuantity,
            itemPrice = item.itemPrice,
            selectedQuantity = quantity
        )

    fun updateItemCheck(checked: Boolean, index: Int) {
        if (!checkIsItemIndexValid(index)) {
            return
        }

        viewModelScope.launch(Dispatchers.Main) {
            _expenseDetailList.emit(
                _expenseDetailList.value.toMutableList().also {
                    it[index] = getItemWithEnabled(it[index], checked)
                }
            )
        }
    }

    fun updateSelectedQuantity(quantity: Int, index: Int) {
        if (!checkIsItemIndexValid(index)) {
            return
        }

        viewModelScope.launch(Dispatchers.Main) {
            _expenseDetailList.emit(
                _expenseDetailList.value.toMutableList().also {
                    it[index] = getItemWithQuantity(it[index], quantity)
                }
            )
        }
    }

    private fun checkIsItemIndexValid(index: Int): Boolean =
        index >= 0 && index < _expenseDetailList.value.count()
}
