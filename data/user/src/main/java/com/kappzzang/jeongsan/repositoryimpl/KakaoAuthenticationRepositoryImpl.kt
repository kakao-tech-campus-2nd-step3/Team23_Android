package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.api.KakaoAuthRetrofitService
import com.kappzzang.jeongsan.api.KakaoSocialApi
import com.kappzzang.jeongsan.data.AuthData
import com.kappzzang.jeongsan.entity.KakaoRefreshTokenPayloadDTO
import com.kappzzang.jeongsan.mapper.KakaoOAuthTokenAuthDataMapper.mapRefreshDtoToAuthData
import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import com.kappzzang.jeongsan.user.BuildConfig
import javax.inject.Inject

class KakaoAuthenticationRepositoryImpl @Inject constructor(private val kakaoApi: KakaoAuthRetrofitService) :
    KakaoAuthenticationRepository {
    override suspend fun refreshKakaoToken(authData: AuthData): AuthData {
        val response =
            kakaoApi.refreshToken(
                AUTH_RETROFIT_CONTENT_TYPE,
                body = KakaoRefreshTokenPayloadDTO(
                    refreshToken = authData.kakaoRefreshToken,
                    clientId = BuildConfig.KAKAO_REST_API_KEY,
                    grantType = AUTH_RETROFIT_REFRESH_GRANT_TYPE
                )
            )
        response.body()?.let {
            return mapRefreshDtoToAuthData(it)
        } ?: let {
            return AuthData("", 0L, "", null)
        }
    }

    companion object {
        const val AUTH_RETROFIT_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8"
        const val AUTH_RETROFIT_REFRESH_GRANT_TYPE = "refresh_token"
    }
}
