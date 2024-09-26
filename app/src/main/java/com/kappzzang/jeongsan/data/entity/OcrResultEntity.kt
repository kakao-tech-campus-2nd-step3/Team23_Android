package com.kappzzang.jeongsan.data.entity

data class OcrResultItem(val name: String, val quantity: Int, val unitPrice: Int)

data class OcrResultEntity(
    val title: String,
    val paymentTime: String,
    val items: List<OcrResultItem>
)
