package com.kappzzang.jeongsan.entity

import kotlinx.serialization.Serializable
import com.google.gson.annotations.SerializedName

@Serializable
data class KakaoRefreshTokenPayloadDTO(
    @SerializedName("grant_type")
    val grantType: String = "refresh_token",
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("refresh_token")
    val refreshToken: String
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