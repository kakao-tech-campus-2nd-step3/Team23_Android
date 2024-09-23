package com.kappzzang.jeongsan.domain.model

import java.util.Date

enum class ExpenseState { CONFIRMED, NOT_CONFIRMED, TRANSFER_PENDING, TRANSFERED }

data class ExpenseItem(
    val id: String,
    val name: String,
    val payer: MemberItem,
    val price: Int,
    val date: Date,
    val state: ExpenseState,
    val categoryColor: String
)
