package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.api.KakaoSocialApi
import com.kappzzang.jeongsan.data.AuthData
import com.kappzzang.jeongsan.mapper.KakaoOAuthTokenAuthDataMapper.mapRefreshDtoToAuthData
import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import com.kappzzang.jeongsan.user.BuildConfig
import javax.inject.Inject

internal class KakaoAuthenticationRepositoryImpl @Inject constructor(private val kakaoApi: KakaoSocialApi) :
    KakaoAuthenticationRepository {
    override suspend fun refreshKakaoToken(authData: AuthData): AuthData {
        val response =
            kakaoApi.refreshAccessToken(authData.kakaoRefreshToken, BuildConfig.KAKAO_REST_API_KEY)
        response.body()?.let {
            return mapRefreshDtoToAuthData(it)
        } ?: let {
            return AuthData("", 0L, "", null)
        }
    }
}
