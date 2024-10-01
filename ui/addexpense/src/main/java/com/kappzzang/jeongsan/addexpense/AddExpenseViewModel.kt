package com.kappzzang.jeongsan.addexpense

import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.ByteArrayOutputStream
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
    private val uploadExpenseUseCase: usecase.UploadExpenseUseCase
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

    fun setInitialReceiptData(bitmap: Bitmap, ocrResult: model.OcrResultResponse.OcrSuccess) {
        viewModelScope.launch(Dispatchers.Main) {
            _expenseImageBitmap.emit(bitmap)
            expenseName.emit(ocrResult.name)
            _expenseItemList.emit(
                ocrResult.detailItems.map {
                    ExpenseItemInput(it.itemName, it.itemPrice, it.itemQuantity)
                } + _expenseItemList.value
            )
        }
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
            imageBase64 = convertBitmapToBase64(_expenseImageBitmap.value),
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

    private fun convertBitmapToBase64(bitmap: Bitmap?): String? {
        if (bitmap == null) {
            return null
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
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
