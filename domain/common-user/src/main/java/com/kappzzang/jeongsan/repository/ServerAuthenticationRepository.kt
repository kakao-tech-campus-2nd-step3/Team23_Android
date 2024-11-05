package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.data.ServerAuthData

interface ServerAuthenticationRepository {
    fun loginToServer(email: String) : ServerAuthData

    fun registerToServer(nickname: String, email: String, profileImageUrl: String) : ServerAuthData

    fun refreshJwtFromServer(authData: ServerAuthData): ServerAuthData
}
