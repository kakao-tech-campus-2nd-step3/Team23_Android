package com.kappzzang.jeongsan.util

import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.data.ServerAuthData

interface AuthenticationRepository {
    fun getKakaoAuthData(): KakaoAuthData

    fun getServerAuthData(): ServerAuthData

    fun updateKakaoAuthData(newData: KakaoAuthData)

    fun updateServerAuthData(newData: ServerAuthData)

    fun removeKakaoAuthData()

    fun removeServerAuthData()

    suspend fun refreshJwtFromServer(authData: ServerAuthData): Result<ServerAuthData>

    // TODO: 추후에 EncryptedSharedPreferences를 사용하여 동기적으로 처리하기
    fun getUuid(): Flow<String>

    suspend fun updateUuid(uuid: String)

    suspend fun removeUuid()
}
