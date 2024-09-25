package com.kappzzang.jeongsan.ui.expensedetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.domain.model.ExpenseDetailItem
import com.kappzzang.jeongsan.domain.model.ExpenseItem
import com.kappzzang.jeongsan.domain.model.ExpenseState
import com.kappzzang.jeongsan.domain.usecase.GetExpenseDetailUseCase
import com.kappzzang.jeongsan.domain.usecase.GetExpenseUseCase
import com.kappzzang.jeongsan.ui.Member
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val getExpenseDetailUseCase: GetExpenseDetailUseCase,
    private val getExpenseUseCase: GetExpenseUseCase
) : ViewModel() {
    private val _expenseDetailList = MutableStateFlow(listOf<ExpenseDetailItem>())
    private val _expense = MutableStateFlow(getBlankExpense())

    val expenseDetailList: StateFlow<List<ExpenseDetailItem>> = _expenseDetailList.asStateFlow()
    val expense: StateFlow<ExpenseItem> = _expense.asStateFlow()

    init {
        initExpense()
        initExpenseDetailList()
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

    private fun getBlankExpense() = ExpenseItem(
        "",
        "",
        Member(""),
        0,
        "",
        Date(),
        ExpenseState.NOT_CONFIRMED,
        ""
    )

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
