package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.data.KakaoAuthData

interface KakaoAuthenticationRepository {
    suspend fun refreshKakaoToken(authData: KakaoAuthData): KakaoAuthData
}
