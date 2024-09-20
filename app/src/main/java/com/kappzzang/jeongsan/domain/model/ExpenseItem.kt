package com.kappzzang.jeongsan.domain.model

import com.kappzzang.jeongsan.ui.Member
import java.util.Date

enum class ExpenseState { CONFIRMED, NOT_CONFIRMED, TRANSFER_PENDING, TRANSFERED }

data class ExpenseItem(
    val id: String,
    val name: String,
    val payer: Member,
    val price: Int,
    val date: Date,
    val state: ExpenseState,
    val categoryColor: String
)
