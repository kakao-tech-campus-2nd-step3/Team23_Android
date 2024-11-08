package com.kappzzang.jeongsan.model

data class ExpenseSelectionStatusItem(
    val itemId: String,
    val name: String,
    val quantity: Int,
    val unitPrice: Int,
    val selectorList: List<ExpenseSelectorInfo>
) {
    val totalPrice: Int
        get() = getTotalPrice()
    val totalSelection: Int
        get() = getTotalSelection()

    private fun getTotalPrice(): Int = unitPrice * quantity
    private fun getTotalSelection(): Int = selectorList.sumOf { it.selectedQuantity }
    fun getTotalPriceForSelectedQuantity(selectedQuantity: Int) =
        (getTotalPrice().toDouble() * selectedQuantity / totalSelection).toInt()
}
