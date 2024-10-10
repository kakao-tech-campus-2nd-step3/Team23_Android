package com.kappzzang.jeongsan.data

enum class KakaoAuthorizeScope(val value: String) {
    EMAIL("account_email") {
        override fun toString(): String = value
    },
    PROFILE("profile_image") {
        override fun toString(): String = value
    },
    NICKNAME("profile_nickname") {
        override fun toString(): String = value
    },
    SEND_EMAIL("account_email") {
        override fun toString(): String = value
    }
}


data class KakaoAuthorizeScopes(
    val scopes: List<KakaoAuthorizeScope>
)