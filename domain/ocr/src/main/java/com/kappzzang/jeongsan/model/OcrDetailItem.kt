package com.kappzzang.jeongsan.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OcrDetailItem(val itemName: String, val itemPrice: Int, val itemQuantity: Int) :
    Parcelable
