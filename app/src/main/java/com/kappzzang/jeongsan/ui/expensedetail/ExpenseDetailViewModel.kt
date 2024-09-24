package com.kappzzang.jeongsan.ui.expensedetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.domain.model.ExpenseDetailItem
import com.kappzzang.jeongsan.domain.usecase.GetExpenseDetailUseCase
import com.kappzzang.jeongsan.domain.usecase.GetExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val getExpenseDetailUseCase: GetExpenseDetailUseCase,
    private val getExpenseUseCase: GetExpenseUseCase
) : ViewModel() {
    private val _expenseItemList = MutableStateFlow(listOf<ExpenseDetailItem>())
    private val _expenseName = MutableStateFlow("")

    val expenseItemList: StateFlow<List<ExpenseDetailItem>> = _expenseItemList.asStateFlow()
    val expenseName: StateFlow<String> = _expenseName.asStateFlow()

    init {
        initExpenseName()
        initExpenseItemList()
    }

    private fun initExpenseName() {
        viewModelScope.launch(Dispatchers.IO) {
            // 추후 전달할 Id
            val expenseId = 10L
            _expenseName.value = getExpenseUseCase.invoke(expenseId).name
        }
    }

    private fun initExpenseItemList() {
        viewModelScope.launch(Dispatchers.IO) {
            _expenseItemList.value = getExpenseDetailUseCase.invoke()
        }
    }


    private fun getItemWithEnabled(item: ExpenseDetailItem, enabled: Boolean): ExpenseDetailItem {
        if (enabled) {
            return if (item.selectedQuantity > 0) {
                item
            } else {
                ExpenseDetailItem(
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
                ExpenseDetailItem(
                    id = item.id,
                    itemName = item.itemName,
                    itemQuantity = item.itemQuantity,
                    itemPrice = item.itemPrice,
                    selectedQuantity = 0
                )
            }
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
        if (index < 0 || index >= _expenseItemList.value.count()) {
            Log.e("Jeongsan", "Invalid index")
            return
        }

        viewModelScope.launch(Dispatchers.Main) {
            _expenseItemList.emit(
                _expenseItemList.value.toMutableList().also {
                    it[index] = getItemWithEnabled(it[index], checked)
                }
            )
        }
    }

    fun updateSelectedQuantity(quantity: Int, index: Int) {
        Log.d("Jeongsan", "quantity changed")
        if (index < 0 || index >= _expenseItemList.value.count()) {
            Log.e("Jeongsan", "Invalid index")
            return
        }

        viewModelScope.launch(Dispatchers.Main) {
            _expenseItemList.emit(
                _expenseItemList.value.toMutableList().also {
                    it[index] = getItemWithQuantity(it[index], quantity)
                }
            )
        }
    }
}
