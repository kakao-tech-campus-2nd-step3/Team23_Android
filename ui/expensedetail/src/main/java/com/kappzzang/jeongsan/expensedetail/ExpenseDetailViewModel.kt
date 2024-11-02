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
    private val getExpenseUseCase: GetExpenseUseCase,
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
        }
    }

    fun updateExpenseIdAndInit(id: String) {
        expenseId.value = id
        initExpense()
    }

    private fun initExpense() {
        viewModelScope.launch(ioDispatcher) {
            // 추후 전달할 Id
            val expenseId = 10L
            _expense.value = getExpenseUseCase.invoke(expenseId)
        }
    }

    private fun initExpenseDetailList() {
        viewModelScope.launch(ioDispatcher) {
            _expenseDetailList.value = getExpenseDetailUseCase.invoke()
        }
    }

    private fun getItemWithEnabled(item: ExpenseDetailItem, enabled: Boolean): ExpenseDetailItem {
        return if (enabled) {
            item.copy( selectedQuantity = 1 )
        } else {
            item.copy( selectedQuantity = 0 )
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
        Log.d("Jeongsan", "checked")
        if (index < 0 || index >= _expenseDetailList.value.count()) {
            Log.e("Jeongsan", "Invalid index")
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
        Log.d("Jeongsan", "quantity changed")
        if (index < 0 || index >= _expenseDetailList.value.count()) {
            Log.e("Jeongsan", "Invalid index")
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
}
