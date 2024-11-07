package com.kappzzang.jeongsan.retrofit

import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AuthInterceptor @Inject constructor(private val authRepository: AuthenticationRepository) :
    Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val originRequest = response.request()
        if (originRequest.header(AUTH_HEADER_KEY).isNullOrEmpty()) {
            return null
        }

        // 기존의 authData
        val authData = runBlocking { authRepository.getServerAuthData().first() }

        // 서버에 새로운 토큰을 요청
        val newAuthData = runBlocking {
            authRepository.refreshJwtFromServer(authData).getOrElse {
                authRepository.removeServerAuthData()
                return@runBlocking null
            }
        } ?: return null

        // 새로운 토큰을 저장
        runBlocking {
            authRepository.updateServerAuthData(newAuthData)
        }

        return originRequest.newBuilder()
            .removeHeader(AUTH_HEADER_KEY)
            .addHeader(AUTH_HEADER_KEY, "Bearer ${newAuthData.accessToken}")
            .build()
    }

    companion object {
        private const val AUTH_HEADER_KEY = "Authorization"
    }
}
