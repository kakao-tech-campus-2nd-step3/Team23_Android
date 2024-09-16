package com.kappzzang.jeongsan.ui.addexpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private fun createDemoItem(index: Int): ExpenseItemInput = ExpenseItemInput(
    itemName = "item $index",
    itemPrice = 100 * index,
    itemQuantity = index % 4 + 1
)

private fun createDemoList(): List<ExpenseItemInput> = listOf(
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
)

class AddExpenseViewModel : ViewModel() {
    private val _expenseItemList by lazy {
        MutableStateFlow(
            listOf(
                ExpenseItemInput(null, null, null)
            )
        )
    }

    private val _manualMode = MutableStateFlow(true)
    private val _expenseName = MutableStateFlow("Demo")
    private val _uploadedImage = MutableStateFlow(false)


    val manualMode: StateFlow<Boolean> = _manualMode.asStateFlow()
    val uploadedImage: StateFlow<Boolean> = _uploadedImage.asStateFlow()
    val expenseItemList: StateFlow<List<ExpenseItemInput>> = _expenseItemList.asStateFlow()
    val expenseName: StateFlow<String> = _expenseName.asStateFlow()

    fun setManualMode(mode: ManualMode) {
        viewModelScope.launch(Dispatchers.Main) {
            _manualMode.emit(mode == ManualMode.MANUAL)
            _uploadedImage.emit(mode == ManualMode.RECEIPT)
        }
    }

    fun setInitialReceiptData() {
        // TODO: 영수증 모델 받아서 UI 정보 업데이트
    }

    fun initiateDemoData() {
        viewModelScope.launch(Dispatchers.Main) {
            insertExpenseItemList(createDemoList())
        }
    }

    private suspend fun insertExpenseItemList(expenseItemList: List<ExpenseItemInput>) {
        _expenseItemList.emit(
            expenseItemList + _expenseItemList.value
        )
    }

    private suspend fun createEmptyList() {
        _expenseItemList.emit(listOf<ExpenseItemInput>())
    }

    companion object {
        enum class ManualMode { MANUAL, RECEIPT }
    }
}