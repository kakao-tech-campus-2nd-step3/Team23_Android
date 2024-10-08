package com.kappzzang.jeongsan.repository

interface KakaoAuthorizationRepository {
    suspend fun getAuthorizationToken(): String
}