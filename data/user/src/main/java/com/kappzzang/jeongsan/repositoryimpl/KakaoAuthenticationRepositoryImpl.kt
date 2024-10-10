package com.kappzzang.jeongsan.repositoryimpl

import android.util.Log
import com.kappzzang.jeongsan.data.AuthData
import com.kappzzang.jeongsan.datasource.KakaoAuthenticationDataSource
import com.kappzzang.jeongsan.mapper.KakaoOAuthTokenAuthDataMapper.mapRefreshDtoToAuthData
import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import javax.inject.Inject

class KakaoAuthenticationRepositoryImpl @Inject constructor(
    private val dataSource: KakaoAuthenticationDataSource
) : KakaoAuthenticationRepository {
    override suspend fun refreshKakaoToken(authData: AuthData): AuthData {
        val response = dataSource.refreshKakaoToken(authData.kakaoRefreshToken)

        if (!response.isSuccessful) {
            Log.e("KSC", "Refresh Failed")
            Log.e("KSC", "message: ${response.errorBody()?.string()}")
        }

        response.body()?.let {
            return mapRefreshDtoToAuthData(it)
        } ?: let {
            return AuthData("", 0L, "", null)
        }
    }
}
