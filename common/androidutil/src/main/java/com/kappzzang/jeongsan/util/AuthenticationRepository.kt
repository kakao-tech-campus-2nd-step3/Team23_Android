package com.kappzzang.jeongsan.util

import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.data.ServerAuthData
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun getKakaoAuthData(): Flow<KakaoAuthData>

    fun getServerAuthData(): Flow<ServerAuthData>

    suspend fun updateKakaoAuthData(newData: KakaoAuthData)

    suspend fun updateServerAuthData(newData: ServerAuthData)

    suspend fun removeKakaoAuthData()

    suspend fun removeServerAuthData()
}
