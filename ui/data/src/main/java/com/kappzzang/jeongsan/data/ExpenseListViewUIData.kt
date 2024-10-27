package com.kappzzang.jeongsan.data

data class ExpenseListViewUIData(
    val totalPriceText: String,
    val priceToSendText: String,
    val expenseItems: List<ExpenseUiItem>
){
    companion object {
        val emptyData = ExpenseListViewUIData(
            "",
            "",
            emptyList()
        )
    }
}
