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

    fun getUuid(): String

    fun updateUuid(uuid: String)

    fun removeUuid()
}
