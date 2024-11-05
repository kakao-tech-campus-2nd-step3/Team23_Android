package com.kappzzang.jeongsan.data

import com.kappzzang.jeongsan.model.ExpenseDetailItem

data class ExpenseDetailUIData(
    val id: String,
    val itemName: String,
    val itemPrice: Int,
    val itemQuantity: Int,
    val selectedQuantity: Int,
    val formEnabled: Boolean
) {
    fun toExpenseDetailItem(): ExpenseDetailItem = ExpenseDetailItem(
        id = this.id,
        itemName = this.itemName,
        itemPrice = this.itemPrice,
        itemQuantity = this.itemQuantity,
        selectedQuantity = this.selectedQuantity
    )
}

fun ExpenseDetailItem.toUIData(formEnabled: Boolean): ExpenseDetailUIData = ExpenseDetailUIData(
    id = this.id,
    itemName = this.itemName,
    itemPrice = this.itemPrice,
    itemQuantity = this.itemQuantity,
    selectedQuantity = this.selectedQuantity,
    formEnabled = formEnabled
)
