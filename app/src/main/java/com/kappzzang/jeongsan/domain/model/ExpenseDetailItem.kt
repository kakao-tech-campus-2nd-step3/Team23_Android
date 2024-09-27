package com.kappzzang.jeongsan.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExpenseDetailItem(
    val id: String,
    val itemName: String,
    val itemPrice: Int,
    val itemQuantity: Int,
    val selectedQuantity: Int
) : Parcelable
