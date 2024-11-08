package com.kappzzang.jeongsan.expensedetail.selectionstatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.data.ExpenseSelectionInfoUIData
import com.kappzzang.jeongsan.data.SelectionInfoItem
import com.kappzzang.jeongsan.model.ExpenseSelectionStatus
import com.kappzzang.jeongsan.usecase.GetExpenseSelectionStatusUseCase
import com.kappzzang.jeongsan.util.IntegerFormatter.formatDecimalSeparator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SelectionStatusViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val expenseSelectionStatusUseCase: GetExpenseSelectionStatusUseCase
) : ViewModel() {
    private var isViewModelInitialized: Boolean = false
    private val expenseSelectionStatus =
        MutableStateFlow(ExpenseSelectionStatus.EMPTY)

    val uiData = expenseSelectionStatus.map {
        ExpenseSelectionInfoUIData(mapSelectionStatusItemsToUIItemList(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ExpenseSelectionInfoUIData(emptyList())
    )

    fun initExpenseId(expenseId: String) {
        if (isViewModelInitialized) {
            return
        }
        viewModelScope.launch(ioDispatcher) {
            expenseSelectionStatusUseCase(expenseId).onSuccess {
                expenseSelectionStatus.emit(it)
            }
        }
    }

    private fun mapSelectionStatusItemsToUIItemList(
        status: ExpenseSelectionStatus
    ): List<SelectionInfoItem> {
        val list = mutableListOf<SelectionInfoItem>()
        for (item in status.items) {
            // 각 아이템 별 Header 생성
            list.add(
                SelectionInfoItem.Header(
                    item.name,
                    CURRENCY_PREFIX + item.totalPrice.formatDecimalSeparator() + CURRENCY_POSTFIX
                )
            )

            // 아이템의 선택 인원 별 SelectorItem 생성
            for (selector in item.selectorList) {
                list.add(
                    SelectionInfoItem.SelectorItem(
                        name = selector.name,
                        quantityText = selector.selectedQuantity.toString(),
                        imageUrl = selector.profileImageUrl,
                        priceText = item.getTotalPriceForSelectedQuantity(selector.selectedQuantity)
                            .formatDecimalSeparator()
                    )
                )
            }

            // Divider 생성
            list.add(SelectionInfoItem.Divider)
        }

        // 마지막 Divider는 제거
        list.removeLastOrNull()
        return list
    }

    companion object {
        const val CURRENCY_POSTFIX = " 원"
        const val CURRENCY_PREFIX = "총 "
    }
}
