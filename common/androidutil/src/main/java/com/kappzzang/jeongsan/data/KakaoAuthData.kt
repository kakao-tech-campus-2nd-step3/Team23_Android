package com.kappzzang.jeongsan.data

data class KakaoAuthData(
    val kakaoAccessToken: String,
    val accessTokenExpirationTime: Long,
    val kakaoRefreshToken: String
)
