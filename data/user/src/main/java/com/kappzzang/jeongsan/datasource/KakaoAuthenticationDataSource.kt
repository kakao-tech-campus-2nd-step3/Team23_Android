package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.api.KakaoAuthRetrofitService
import com.kappzzang.jeongsan.entity.KakaoRefreshTokenPayloadDTO
import com.kappzzang.jeongsan.entity.KakaoRefreshTokenResponseDTO
import com.kappzzang.jeongsan.user.BuildConfig
import javax.inject.Inject
import retrofit2.Response

class KakaoAuthenticationDataSource @Inject constructor(
    private val kakaoApi: KakaoAuthRetrofitService
) {

    suspend fun refreshKakaoToken(
        kakaoRefreshToken: String
    ): Response<KakaoRefreshTokenResponseDTO> {
        val response =
            kakaoApi.refreshToken(
                AUTH_RETROFIT_CONTENT_TYPE,
                body = KakaoRefreshTokenPayloadDTO(
                    refreshToken = kakaoRefreshToken,
                    clientId = BuildConfig.KAKAO_REST_API_KEY,
                    grantType = AUTH_RETROFIT_REFRESH_GRANT_TYPE
                ),
                refreshToken = kakaoRefreshToken,
                clientId = BuildConfig.KAKAO_REST_API_KEY,
                grantType = AUTH_RETROFIT_REFRESH_GRANT_TYPE,
                clientSecret = ""
            )
        return response
    }

    companion object {
        const val AUTH_RETROFIT_CONTENT_TYPE = "application/json;charset=utf-8"
        const val AUTH_RETROFIT_REFRESH_GRANT_TYPE = "refresh_token"
    }
}
