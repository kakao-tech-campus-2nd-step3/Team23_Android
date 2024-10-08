package com.kappzzang.jeongsan.model

import com.kappzzang.jeongsan.data.AuthData

sealed class AuthenticationResult {
    data class AuthenticationSuccess(val authData: AuthData) : AuthenticationResult()

    data object NoToken : AuthenticationResult()

    data class AuthenticationError(val message: String) : AuthenticationResult()

    data object RefreshTokenExpired : AuthenticationResult()
}
