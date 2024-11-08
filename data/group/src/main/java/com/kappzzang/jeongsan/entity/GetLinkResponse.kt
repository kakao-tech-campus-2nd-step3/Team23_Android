package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName

data class GetLinkResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("errorCode")
    val errorCode: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: KakaoPayLink
) {
    val link: String
        get() = data.link
}

data class KakaoPayLink(
    @SerializedName("kakaoPayLink")
    val link: String
)
