package com.kappzzang.jeongsan.util

import com.kappzzang.jeongsan.data.AuthData

interface AuthenticationRepository {
    suspend fun getAuthData(): AuthData

    suspend fun updateAuthData(newData: AuthData)

    suspend fun checkHasAuthData(): Boolean

    suspend fun removeAuthData()
}
