package com.kappzzang.jeongsan.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class KakaoRefreshTokenPayloadDTO(
    @SerializedName("grant_type")
    val grantType: String,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("client_secret")
    val clientSecret: String = ""
)

@Serializable
data class KakaoRefreshTokenResponseDTO(
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val accessTokenExpiresInSeconds: Int,
    @SerializedName("refresh_token")
    val refreshToken: String?,
    @SerializedName("refresh_token_expires_in")
    val refreshTokenExpiresInSeconds: Int?
)
