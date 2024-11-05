package com.kappzzang.jeongsan.repository

import com.kappzzang.jeongsan.data.KakaoAuthData

interface ServerAuthenticationRepository {
    fun registerToServer(authData: KakaoAuthData)

    fun getJwtFromServer(authData: KakaoAuthData): KakaoAuthData

    fun getSavedJwt(): String
}
