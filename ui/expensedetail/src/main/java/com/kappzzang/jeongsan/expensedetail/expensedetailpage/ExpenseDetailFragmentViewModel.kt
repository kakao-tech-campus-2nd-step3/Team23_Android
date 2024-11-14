package com.kappzzang.jeongsan.expensedetail.expensedetailpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.data.ExpenseDetailUIData
import com.kappzzang.jeongsan.data.toUIData
import com.kappzzang.jeongsan.expensedetail.ExpenseDetailState
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItemWithDetails
import com.kappzzang.jeongsan.usecase.EditExpenseDetailUseCase
import com.kappzzang.jeongsan.usecase.GetExpenseDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ExpenseDetailFragmentViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val getExpenseDetailUseCase: GetExpenseDetailUseCase,
    private val editExpenseDetailUseCase: EditExpenseDetailUseCase
) : ViewModel() {

    private val _expense = MutableStateFlow(ExpenseItemWithDetails.EMPTY)
    private val groupId = MutableStateFlow("")
    private val expenseId = MutableStateFlow("")
    private val formEditable = MutableStateFlow(true)
    private val _expenseDetailSaveState = MutableStateFlow(ExpenseDetailState.IDLE)

    val expenseDetailSaveState = _expenseDetailSaveState.asStateFlow()

    val expenseDetailUIData = combine(
        _expense,
        formEditable
    ) { expense, editable ->
        expense.expenseDetails.map {
            it.toUIData(editable)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = emptyList()
    )

    val expense: StateFlow<ExpenseItemWithDetails> = _expense.asStateFlow()

    fun saveExpenseDetail() {
        if (_expenseDetailSaveState.value != ExpenseDetailState.IDLE) {
            return
        }
        val editedExpenseDetailItemList =
            mapChangedExpenseDetailList(expenseDetailUIData.value)
        _expenseDetailSaveState.value = ExpenseDetailState.UPLOADING
        Log.d("KSC", "Edit List: $editedExpenseDetailItemList")
        uploadEditList(editedExpenseDetailItemList)
    }

    private fun mapChangedExpenseDetailList(
        uiDataList: List<ExpenseDetailUIData>
    ): List<ExpenseDetailItem> = uiDataList.mapNotNull {
        it.toExpenseDetailItem()
    }

    private fun uploadEditList(expenseDetailItemList: List<ExpenseDetailItem>) {
        if (expenseDetailItemList.isEmpty()) {
            _expenseDetailSaveState.value = ExpenseDetailState.SUCCESS
            return
        }
        viewModelScope.launch(ioDispatcher) {
            editExpenseDetailUseCase.invoke(
                expenseDetailItemList,
                expenseId.value,
                groupId = groupId.value
            ).onSuccess {
                _expenseDetailSaveState.value = ExpenseDetailState.SUCCESS
            }.onFailure {
                _expenseDetailSaveState.value = ExpenseDetailState.FAILED
            }
        }
    }

    fun setInitialData(expenseId: String, groupId: String, editable: Boolean) {
        if(this.expenseId.value.isNotEmpty()){
            return
        }
        this.expenseId.value = expenseId
        this.groupId.value = groupId
        formEditable.value = editable
        initExpense()
    }

    private fun initExpense() {
        viewModelScope.launch(ioDispatcher) {
            val result = getExpenseDetailUseCase.invoke(expenseId.value)
            result.onSuccess {
                _expense.value = it
            }
                .onFailure {
                    // TODO: Expense Detail 조회 실패 시 예외처리
                }
        }
    }

    private fun getItemWithEnabled(item: ExpenseDetailItem, enabled: Boolean): ExpenseDetailItem =
        if (enabled) {
            item.copy(selectedQuantity = 1)
        } else {
            item.copy(selectedQuantity = 0)
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
        if (!checkIsItemIndexValid(index)) {
            return
        }

        viewModelScope.launch(Dispatchers.Main) {
            val modifiedExpense = _expense.value.copy(
                expenseDetails = _expense.value.expenseDetails.toMutableList().also {
                    it[index] = getItemWithEnabled(it[index], checked)
                }
            )
            _expense.emit(modifiedExpense)
        }
    }

    fun updateSelectedQuantity(quantity: Int, index: Int) {
        if (!checkIsItemIndexValid(index)) {
            return
        }

        viewModelScope.launch(Dispatchers.Main) {
            val modifiedExpense = _expense.value.copy(
                expenseDetails = _expense.value.expenseDetails.toMutableList().also {
                    it[index] = getItemWithQuantity(it[index], quantity)
                }
            )
            _expense.emit(modifiedExpense)
        }
    }

    private fun checkIsItemIndexValid(index: Int): Boolean =
        index >= 0 && index < _expense.value.expenseDetails.count()
}
