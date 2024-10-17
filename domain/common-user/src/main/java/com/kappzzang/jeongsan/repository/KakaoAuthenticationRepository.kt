package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.data.AuthData

interface KakaoAuthenticationRepository {
    suspend fun refreshKakaoToken(authData: AuthData): AuthData
}
