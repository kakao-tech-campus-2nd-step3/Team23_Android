package com.kappzzang.jeongsan.model

data class ExpenseSelectionStatusItem(
    val itemId: String,
    val name: String,
    val quantity: Int,
    val unitPrice: Int,
    val selectorList: List<ExpenseSelectorInfo>
) {
    val totalPrice: Int = calculateTotalPrice()
    val totalSelection: Int = calculateTotalSelection()

    private fun calculateTotalPrice(): Int = unitPrice * quantity
    private fun calculateTotalSelection(): Int = selectorList.sumOf { it.selectedQuantity }
    fun getTotalPriceForSelectedQuantity(selectedQuantity: Int) =
        (calculateTotalPrice().toDouble() * selectedQuantity / totalSelection).toInt()
}
