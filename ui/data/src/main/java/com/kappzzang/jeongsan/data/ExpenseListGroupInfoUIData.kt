package com.kappzzang.jeongsan.data

data class ExpenseListGroupInfoUIData(
    val groupName: String,
    val groupSubject: String
) {
    companion object {
        val EMPTY = ExpenseListGroupInfoUIData("", "")
    }
}
