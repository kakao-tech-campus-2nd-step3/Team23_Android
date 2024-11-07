package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.data.ServerAuthData

interface ServerAuthenticationRepository {
    suspend fun loginToServer(email: String): Result<ServerAuthData>

    suspend fun registerToServer(
        nickname: String,
        email: String,
        profileImageUrl: String
    ): Result<ServerAuthData>

    suspend fun refreshJwtFromServer(authData: ServerAuthData): Result<ServerAuthData>
}
