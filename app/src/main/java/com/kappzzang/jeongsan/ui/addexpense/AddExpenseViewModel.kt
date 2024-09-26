package com.kappzzang.jeongsan.ui.addexpense

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.domain.model.ReceiptDetailItem
import com.kappzzang.jeongsan.domain.model.ReceiptItem
import com.kappzzang.jeongsan.domain.usecase.UploadExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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
    createDemoItem(4)
)

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val uploadExpenseUseCase: UploadExpenseUseCase
) : ViewModel() {
    private val _expenseItemList by lazy {
        MutableStateFlow(
            listOf(
                ExpenseItemInput(null, null, null)
            )
        )
    }

    private val _expenseImageBitmap = MutableStateFlow<Bitmap?>(null)
    private val _manualMode = MutableStateFlow(true)
    private val _uploadedImage = MutableStateFlow(false)

    val expenseImageBitmap: StateFlow<Bitmap?> = _expenseImageBitmap.asStateFlow()
    val manualMode: StateFlow<Boolean> = _manualMode.asStateFlow()
    val uploadedImage: StateFlow<Boolean> = _uploadedImage.asStateFlow()
    val expenseItemList: StateFlow<List<ExpenseItemInput>> = _expenseItemList.asStateFlow()
    val expenseName = MutableStateFlow("Demo")

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

    fun addNewExpense() {
        viewModelScope.launch(Dispatchers.Main) {
            _expenseItemList.emit(
                _expenseItemList.value + ExpenseItemInput(null, null, null)
            )
        }
    }

    fun removeExpense(removeItemPosition: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            _expenseItemList.emit(
                _expenseItemList.value.filterIndexed { index, _ -> index != removeItemPosition }
            )
        }
    }

    fun uploadExpense(): Boolean {
        if (!checkItemValid()) {
            return false
        }

        val receiptItem = ReceiptItem(
            title = expenseName.value,
            categoryColor = "#FF0000", // TODO: 카테고리 색을 넣도록 UI 수정 필요
            imageBase64 = "Base64 Image", // TODO: 이미지 업로드 후 Base64로 변경
            expenseDetailItemList = _expenseItemList.value.subList(
                0,
                _expenseItemList.value.size - 1
            ).map {
                ReceiptDetailItem(
                    itemName = it.itemName!!,
                    itemPrice = it.itemPrice!!,
                    itemQuantity = it.itemQuantity!!
                )
            }
        )

        viewModelScope.launch(Dispatchers.IO) {
            uploadExpenseUseCase(receiptItem)
        }

        return true
    }

    private fun checkItemValid(): Boolean {
        // 빈 리스트인 경우
        if (_expenseItemList.value.size == 1) {
            return false
        }

        // 마지막 아이템을 제외한 아이템은 하나라도 null이면 안됨
        _expenseItemList.value.subList(0, _expenseItemList.value.size - 1).forEach {
            if (it.itemName == null || it.itemPrice == null || it.itemQuantity == null) {
                return false
            }
        }

        return true
    }

    fun setExpenseImageBitmap(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.Main) {
            _expenseImageBitmap.emit(bitmap)
        }
    }

    companion object {
        enum class ManualMode { MANUAL, RECEIPT }
    }
}
