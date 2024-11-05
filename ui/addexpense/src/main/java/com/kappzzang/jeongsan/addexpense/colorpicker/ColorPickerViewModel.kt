package com.kappzzang.jeongsan.addexpense.colorpicker

import androidx.lifecycle.ViewModel
import com.kappzzang.jeongsan.data.ExpenseCategoryUIItem
import com.kappzzang.jeongsan.model.ExpenseCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ColorPickerViewModel @Inject constructor() : ViewModel() {
    private val _expenseCategoryUIItemList = MutableStateFlow<List<ExpenseCategoryUIItem>>(emptyList())
    private val selectedItemIndex = MutableStateFlow(-1)
    val expenseCategoryUIItemList = _expenseCategoryUIItemList.asStateFlow()

    fun updateUIItemList(expenseCategoryList: List<ExpenseCategory>, selectedId: String) {
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

    fun updateSelectedItemId(id: String) {
        _expenseCategoryUIItemList.value =
            expenseCategoryUIItemList.value.map {
                it.copy(
                    selected = it.id == id
                )
            }
    }
}