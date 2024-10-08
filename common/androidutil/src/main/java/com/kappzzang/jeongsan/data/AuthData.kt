package com.kappzzang.jeongsan.data

data class AuthData(
    val kakaoAccessToken: String,
    val accessTokenExpirationTime: Long,
    val kakaoRefreshToken: String,
    val jwt: String?)
