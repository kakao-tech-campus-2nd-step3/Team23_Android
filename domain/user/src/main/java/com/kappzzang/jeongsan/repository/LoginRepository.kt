package com.kappzzang.jeongsan.repository

interface LoginRepository {
    suspend fun getJwtFromServer(accessToken: String): String

    suspend fun registerToServer(accessToken: String): String
}
