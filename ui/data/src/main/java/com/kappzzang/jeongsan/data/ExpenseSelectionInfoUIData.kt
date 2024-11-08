package com.kappzzang.jeongsan.data

sealed class SelectionInfoItem {
    data class Header(
        val name: String,
        val priceText: String
    ): SelectionInfoItem()

    data class SelectorItem(
        val imageUrl: String,
        val name: String,
        val quantityText: String,
        val priceText: String
    ): SelectionInfoItem()

    data object Divider: SelectionInfoItem()
}

data class ExpenseSelectionInfoUIData(
    val selectionInfoItemList: List<SelectionInfoItem>
)
