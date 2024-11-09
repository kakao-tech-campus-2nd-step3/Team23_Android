package com.kappzzang.jeongsan.addexpense

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.data.ExpenseItemInput
import com.kappzzang.jeongsan.model.ExpenseCategory
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.model.ReceiptDetailItem
import com.kappzzang.jeongsan.model.ReceiptItem
import com.kappzzang.jeongsan.usecase.GetCategoryListUseCase
import com.kappzzang.jeongsan.usecase.UploadExpenseUseCase
import com.kappzzang.jeongsan.util.Base64BitmapEncoder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ExpenseUploadingProgress { NOT_STARTED, UPLOADING, UPLOAD_SUCCESS, UPLOAD_FAILED }

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val uploadExpenseUseCase: UploadExpenseUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val getCategoryListUseCase: GetCategoryListUseCase
) : ViewModel() {
    private val _expenseItemList by lazy {
        MutableStateFlow(
            listOf(
                ExpenseItemInput(null, null, null, true)
            )
        )
    }

    private val _selectedCategory = MutableStateFlow(ExpenseCategory("", "#ffffff", ""))
    private val _inputsLocked = MutableStateFlow(false)
    private val _createdExpenseId = MutableStateFlow("")
    private val _categoryList by lazy { getCategoryList() }
    private val _uploadingProgress = MutableStateFlow(ExpenseUploadingProgress.NOT_STARTED)
    private val _expenseImageBitmap = MutableStateFlow<Bitmap?>(null)
    private val _manualMode = MutableStateFlow(true)
    private val _uploadedImage = MutableStateFlow(false)
    private val _groupId = MutableStateFlow("")

    val inputsLocked = _inputsLocked.asStateFlow()
    val uploadingProgress = _uploadingProgress.asStateFlow()
    val expenseImageBitmap: StateFlow<Bitmap?> = _expenseImageBitmap.asStateFlow()
    val manualMode: StateFlow<Boolean> = _manualMode.asStateFlow()
    val uploadedImage: StateFlow<Boolean> = _uploadedImage.asStateFlow()
    val expenseItemList: StateFlow<List<ExpenseItemInput>> =
        _expenseItemList.asStateFlow()
    val expenseName = MutableStateFlow("Demo")
    val groupId = _groupId.asStateFlow()
    val createdExpenseId = _createdExpenseId.asStateFlow()

    var selectedCategory = _selectedCategory.asStateFlow()
    val categoryList = _categoryList.asStateFlow()

    private fun getCategoryList(): MutableStateFlow<List<ExpenseCategory>> {
        val mList = MutableStateFlow<List<ExpenseCategory>>(emptyList())
        viewModelScope.launch(Dispatchers.IO) {
            getCategoryListUseCase.invoke().onSuccess {
                mList.emit(it)
                it.lastOrNull()?.let { item ->
                    updateSelectedCategory(item.id)
                }
            }
        }
        return mList
    }

    fun setManualMode(mode: ManualMode) {
        viewModelScope.launch(Dispatchers.Main) {
            _manualMode.emit(mode == ManualMode.MANUAL)
            _uploadedImage.emit(mode == ManualMode.RECEIPT)
        }
    }

    fun setInitialReceiptData(bitmap: Bitmap, ocrResult: OcrResultResponse.OcrSuccess) {
        viewModelScope.launch(Dispatchers.Main) {
            _expenseImageBitmap.emit(bitmap)
            expenseName.emit(ocrResult.name)
            _expenseItemList.emit(
                ocrResult.detailItems.map {
                    ExpenseItemInput(
                        it.itemName,
                        it.itemPrice,
                        it.itemQuantity,
                        false
                    )
                } + _expenseItemList.value
            )
        }
    }

    fun initGroupId(groupId: String) {
        this._groupId.value = groupId
    }

    fun addNewExpense() {
        viewModelScope.launch(Dispatchers.Main) {
            val currentExpenseItemList = _expenseItemList.value.toMutableList()
            currentExpenseItemList[currentExpenseItemList.size - 1].isPlaceholder = false
            currentExpenseItemList.add(ExpenseItemInput(null, null, null, true))
            _expenseItemList.emit(currentExpenseItemList)
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
        if (uploadingProgress.value == ExpenseUploadingProgress.UPLOADING) {
            return true
        }

        _uploadingProgress.value = ExpenseUploadingProgress.UPLOADING

        val receiptItem = ReceiptItem(
            title = expenseName.value,
            categoryId = selectedCategory.value.id,
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

        viewModelScope.launch(ioDispatcher) {
            uploadExpenseUseCase(receiptItem, _groupId.value)
                .onSuccess {
                    _createdExpenseId.emit(it)
                    _uploadingProgress.emit(ExpenseUploadingProgress.UPLOAD_SUCCESS)
                }
                .onFailure {
                    _uploadingProgress.emit(ExpenseUploadingProgress.UPLOAD_FAILED)
                }
        }

        return true
    }

    fun setInputsLock(locked: Boolean) {
        _inputsLocked.value = locked
    }

    fun convertBitmapToBase64(bitmap: Bitmap?): String? {
        if (bitmap == null) {
            return null
        }

        return Base64BitmapEncoder.convertBitmapToBase64String(bitmap)
    }

    fun updateSelectedCategory(categoryId: String) {
        categoryList.value.find { it.id == categoryId }?.let {
            _selectedCategory.value = it
        }
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
        const val UNDEFINED_COLOR = "#000000"
        enum class ManualMode { MANUAL, RECEIPT }
    }
}
