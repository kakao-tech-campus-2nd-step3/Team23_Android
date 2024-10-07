package com.kappzzang.jeongsan.model

import com.kappzzang.jeongsan.data.AuthData

sealed class LoginResult {
    data class LoginSuccess(val authData: AuthData) : LoginResult()

    data object NoToken : LoginResult()

    data class LoginError(val message: String) : LoginResult()

    data object RefreshTokenExpired : LoginResult()
}
