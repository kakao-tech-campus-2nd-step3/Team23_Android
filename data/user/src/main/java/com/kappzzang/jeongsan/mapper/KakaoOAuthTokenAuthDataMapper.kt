package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.data.AuthData
import com.kappzzang.jeongsan.entity.KakaoRefreshTokenResponseDTO

object KakaoOAuthTokenAuthDataMapper {
    private fun getExpirationTime(accessTokenExpirationTimeInSeconds: Int): Long =
        System.currentTimeMillis() + accessTokenExpirationTimeInSeconds * 1_000L

    fun mapRefreshDtoToAuthData(
        refreshTokenResponseDTO: KakaoRefreshTokenResponseDTO,
        originalAuthData: AuthData? = null
    ): AuthData {
        val result: AuthData
        if (originalAuthData == null) {
            result = AuthData(
                kakaoRefreshToken = refreshTokenResponseDTO.refreshToken ?: "",
                kakaoAccessToken = refreshTokenResponseDTO.accessToken,
                jwt = "",
                accessTokenExpirationTime = getExpirationTime(
                    refreshTokenResponseDTO.accessTokenExpiresInSeconds
                )
            )
        } else {
            result = if (refreshTokenResponseDTO.refreshToken != null) {
                originalAuthData.copy(
                    kakaoAccessToken = refreshTokenResponseDTO.accessToken,
                    kakaoRefreshToken = refreshTokenResponseDTO.refreshToken,
                    accessTokenExpirationTime = getExpirationTime(
                        refreshTokenResponseDTO.accessTokenExpiresInSeconds
                    )
                )
            } else {
                originalAuthData.copy(
                    kakaoAccessToken = refreshTokenResponseDTO.accessToken,
                    accessTokenExpirationTime = getExpirationTime(
                        refreshTokenResponseDTO.accessTokenExpiresInSeconds
                    )
                )
            }
        }

        return result
    }
}
