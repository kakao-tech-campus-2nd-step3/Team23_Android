package com.kappzzang.jeongsan.entity

data class OcrResultEntity(
    val title: String,
    val paymentTime: String,
    val items: List<OcrResultDetailItem>
)
