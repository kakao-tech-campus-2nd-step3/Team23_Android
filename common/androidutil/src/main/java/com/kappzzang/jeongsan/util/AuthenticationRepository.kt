package com.kappzzang.jeongsan.util

import com.kappzzang.jeongsan.data.AuthData
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun getAuthData(): Flow<AuthData>

    suspend fun updateAuthData(newData: AuthData)

    suspend fun removeAuthData()
}
