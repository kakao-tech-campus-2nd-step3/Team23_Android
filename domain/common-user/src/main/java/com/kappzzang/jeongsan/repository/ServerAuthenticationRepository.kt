package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.data.ServerAuthData

interface ServerAuthenticationRepository {
    suspend fun loginToServer(email: String) : ServerAuthData

    suspend fun registerToServer(nickname: String, email: String, profileImageUrl: String) : ServerAuthData

    suspend fun refreshJwtFromServer(authData: ServerAuthData): ServerAuthData
}
