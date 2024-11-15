package com.kappzzang.jeongsan.data

sealed class AppLoginState {
    data object TryAutoLogin : AppLoginState()

    sealed class Idle : AppLoginState() {
        data object NotKakaoLoggedIn : Idle()
        data object NotServerLoggedIn : Idle()
    }

    data object LoginComplete : AppLoginState()

    data class KakaoLoginFailed(val message: String) : AppLoginState()
    data class ServerLoginFailed(val message: String) : AppLoginState()
}
