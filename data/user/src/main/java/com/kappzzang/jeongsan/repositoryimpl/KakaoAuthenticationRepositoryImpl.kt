package com.kappzzang.jeongsan.repositoryimpl

import android.util.Log
import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.datasource.KakaoAuthenticationDataSource
import com.kappzzang.jeongsan.mapper.KakaoOAuthTokenKakaoAuthDataMapper.mapRefreshDtoToAuthData
import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import javax.inject.Inject

class KakaoAuthenticationRepositoryImpl @Inject constructor(
    private val dataSource: KakaoAuthenticationDataSource
) : KakaoAuthenticationRepository {
    override suspend fun refreshKakaoToken(authData: KakaoAuthData): KakaoAuthData {
        val response = dataSource.refreshKakaoToken(authData.kakaoRefreshToken)

        if (!response.isSuccessful) {
            Log.e("KSC", "Refresh Failed")
            Log.e("KSC", "message: ${response.errorBody()?.string()}")
        }

        response.body()?.let {
            return mapRefreshDtoToAuthData(it)
        } ?: let {
            return KakaoAuthData("", 0L, "")
        }
    }
}
