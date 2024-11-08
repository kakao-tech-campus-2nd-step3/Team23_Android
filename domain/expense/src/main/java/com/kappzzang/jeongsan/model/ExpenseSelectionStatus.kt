package com.kappzzang.jeongsan.model

data class ExpenseSelectionStatus(
    val name: String,
    val items: List<ExpenseSelectionStatusItem>
) {
    companion object{
        val EMPTY = ExpenseSelectionStatus("", emptyList())
    }
}
