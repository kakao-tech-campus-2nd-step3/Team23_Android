package com.kappzzang.jeongsan.expensedetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.usecase.EditExpenseDetailUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseDetailUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val getExpenseDetailUseCase: com.kappzzang.jeongsan.usecase.GetExpenseDetailUseCase,
    private val getExpenseUseCase: com.kappzzang.jeongsan.usecase.GetExpenseUseCase,
    private val editExpenseDetailUseCase: com.kappzzang.jeongsan.usecase.EditExpenseDetailUseCase
) : ViewModel() {
    private val _expenseDetailList = MutableStateFlow(listOf<com.kappzzang.jeongsan.model.ExpenseDetailItem>())
    private val _expense = MutableStateFlow(com.kappzzang.jeongsan.model.ExpenseItem.EMPTY)

    val expenseDetailList: StateFlow<List<com.kappzzang.jeongsan.model.ExpenseDetailItem>> = _expenseDetailList.asStateFlow()
    val expense: StateFlow<com.kappzzang.jeongsan.model.ExpenseItem> = _expense.asStateFlow()

    init {
        initExpense()
        initExpenseDetailList()
    }

    fun saveExpenseDetail() {
        viewModelScope.launch(Dispatchers.IO) {
            editExpenseDetailUseCase.invoke(_expenseDetailList.value)
        }
    }

    private fun initExpense() {
        viewModelScope.launch(Dispatchers.IO) {
            // 추후 전달할 Id
            val expenseId = 10L
            _expense.value = getExpenseUseCase.invoke(expenseId)
        }
    }

    private fun initExpenseDetailList() {
        viewModelScope.launch(Dispatchers.IO) {
            _expenseDetailList.value = getExpenseDetailUseCase.invoke()
        }
    }

    private fun getItemWithEnabled(item: com.kappzzang.jeongsan.model.ExpenseDetailItem, enabled: Boolean): com.kappzzang.jeongsan.model.ExpenseDetailItem {
        if (enabled) {
            return if (item.selectedQuantity > 0) {
                item
            } else {
                com.kappzzang.jeongsan.model.ExpenseDetailItem(
                    id = item.id,
                    itemName = item.itemName,
                    itemQuantity = item.itemQuantity,
                    itemPrice = item.itemPrice,
                    selectedQuantity = 1
                )
            }
        } else {
            return if (item.selectedQuantity == 0) {
                item
            } else {
                com.kappzzang.jeongsan.model.ExpenseDetailItem(
                    id = item.id,
                    itemName = item.itemName,
                    itemQuantity = item.itemQuantity,
                    itemPrice = item.itemPrice,
                    selectedQuantity = 0
                )
            }
        }
    }

    private fun getItemWithQuantity(item: com.kappzzang.jeongsan.model.ExpenseDetailItem, quantity: Int): com.kappzzang.jeongsan.model.ExpenseDetailItem =
        com.kappzzang.jeongsan.model.ExpenseDetailItem(
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