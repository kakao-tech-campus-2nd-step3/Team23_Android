package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.data.AuthData

interface ServerAuthenticationRepository {
    fun registerToServer(authData: AuthData)

    fun getJwtFromServer(authData: AuthData): AuthData
}
