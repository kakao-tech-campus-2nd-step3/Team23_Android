package com.kappzzang.jeongsan.addexpense.colorpicker

import android.util.Log
import androidx.lifecycle.ViewModel
import com.kappzzang.jeongsan.data.ExpenseCategoryUIItem
import com.kappzzang.jeongsan.model.ExpenseCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ColorPickerViewModel @Inject constructor() : ViewModel() {
    private val _expenseCategoryUIItemList =
        MutableStateFlow<List<ExpenseCategoryUIItem>>(emptyList())
    val expenseCategoryUIItemList = _expenseCategoryUIItemList.asStateFlow()

    fun updateUIItemList(expenseCategoryList: List<ExpenseCategory>, selectedId: String) {
        Log.d("KSC", "dialog: ${expenseCategoryList.size}")
        _expenseCategoryUIItemList.value =
            expenseCategoryList.map {
                ExpenseCategoryUIItem(
                    color = it.color,
                    name = it.name,
                    id = it.id,
                    selected = it.id == selectedId
                )
            }
    }

    fun updateSelectedItem(item: ExpenseCategory) {
        _expenseCategoryUIItemList.value =
            expenseCategoryUIItemList.value.map {
                it.copy(
                    selected = it.id == item.id
                )
            }
    }
}
