package com.kappzzang.jeongsan.ui.expensedetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.domain.model.ExpenseDetailItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private fun createDemoItem(index: Int): ExpenseDetailItem = ExpenseDetailItem(
    id = "$index",
    itemName = "아이템 item $index",
    itemPrice = 100 * index,
    itemQuantity = index % 4 + 1,
    selectedQuantity = 0
)

private fun createDemoList(): List<ExpenseDetailItem> = listOf(
    createDemoItem(0),
    createDemoItem(1),
    createDemoItem(2),
    createDemoItem(3),
    createDemoItem(4),
    createDemoItem(0),
    createDemoItem(1),
    createDemoItem(2),
    createDemoItem(3),
    createDemoItem(4),
    createDemoItem(0),
    createDemoItem(1),
    createDemoItem(2),
    createDemoItem(3),
    createDemoItem(4),
    createDemoItem(0),
    createDemoItem(1),
    createDemoItem(2),
    createDemoItem(3),
    createDemoItem(4)
)

private fun createDemoExpenseName(): String = "카페 Demo 123"

class ExpenseDetailViewModel : ViewModel() {
    private val _expenseItemList = MutableStateFlow(createDemoList())

    private val _expenseName = MutableStateFlow(createDemoExpenseName())

    val expenseItemList: StateFlow<List<ExpenseDetailItem>> = _expenseItemList.asStateFlow()
    val expenseName: StateFlow<String> = _expenseName.asStateFlow()

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

    private fun getItemWithQuantity(item: ExpenseDetailItem, quantity: Int): ExpenseDetailItem {
        return ExpenseDetailItem(
            id = item.id,
            itemName = item.itemName,
            itemQuantity = item.itemQuantity,
            itemPrice = item.itemPrice,
            selectedQuantity = quantity
        )
    }

    fun updateItemCheck(checked: Boolean, index: Int) {
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
