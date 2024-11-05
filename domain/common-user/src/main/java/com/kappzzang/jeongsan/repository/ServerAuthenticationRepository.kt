package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.data.ServerAuthData

interface ServerAuthenticationRepository {
    fun loginToServer(authData: ServerAuthData)

    fun registerToServer(authData: ServerAuthData)

    fun refreshJwtFromServer(authData: ServerAuthData): ServerAuthData
}
