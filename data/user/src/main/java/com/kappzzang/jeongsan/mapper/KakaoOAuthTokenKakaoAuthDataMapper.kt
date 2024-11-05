package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.entity.KakaoRefreshTokenResponseDTO

object KakaoOAuthTokenKakaoAuthDataMapper {
    private fun getExpirationTime(accessTokenExpirationTimeInSeconds: Int): Long =
        System.currentTimeMillis() + accessTokenExpirationTimeInSeconds * 1_000L

    fun mapRefreshDtoToAuthData(
        refreshTokenResponseDTO: KakaoRefreshTokenResponseDTO,
        originalAuthData: KakaoAuthData? = null
    ): KakaoAuthData {
        val result: KakaoAuthData
        if (originalAuthData == null) {
            result = KakaoAuthData(
                kakaoRefreshToken = refreshTokenResponseDTO.refreshToken ?: "",
                kakaoAccessToken = refreshTokenResponseDTO.accessToken,
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
