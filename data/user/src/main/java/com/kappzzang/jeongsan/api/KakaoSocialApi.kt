package com.kappzzang.jeongsan.api

import com.kappzzang.jeongsan.entity.KakaoRefreshTokenPayloadDTO
import com.kappzzang.jeongsan.entity.KakaoRefreshTokenResponseDTO
import retrofit2.Response
import javax.inject.Inject

class KakaoSocialApi @Inject constructor(
    private val kakaoAuthRetrofitService: KakaoAuthRetrofitService
) {
    suspend fun refreshAccessToken(
        refreshToken: String,
        clientId: String
    ): Response<KakaoRefreshTokenResponseDTO> {
        return kakaoAuthRetrofitService.refreshToken(
            contentType = AUTH_RETROFIT_CONTENT_TYPE,
            body = KakaoRefreshTokenPayloadDTO(
                refreshToken = refreshToken,
                clientId = clientId,
                grantType = AUTH_RETROFIT_REFRESH_GRANT_TYPE
            )
        )
    }

    companion object {
        const val AUTH_RETROFIT_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8"
        const val AUTH_RETROFIT_REFRESH_GRANT_TYPE = "refresh_token"
    }
}