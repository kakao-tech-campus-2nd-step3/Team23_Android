package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

// expense모듈에 속해야하는 것 같아 구현을 마치지 않음
data class GetMyExpenseResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("errorCode")
    val errorCode: String,
    @SerializedName("message")
    val message: String
)
