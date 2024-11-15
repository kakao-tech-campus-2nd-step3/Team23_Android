package com.kappzzang.jeongsan.retrofit

import com.kappzzang.jeongsan.util.AuthenticationRepository
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor @Inject constructor(private val authRepository: AuthenticationRepository) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (chain.request().headers[SKIP_AUTH_KEY] == SKIP_AUTH_VALUE) {
            val newRequest = chain.request().newBuilder()
                .removeHeader(SKIP_AUTH_KEY)
                .build()
            return chain.proceed(newRequest)
        }

        val serverAuthData = authRepository.getServerAuthData()

        val newRequest = chain.request().newBuilder()
            .addHeader(AUTH_HEADER_KEY, "Bearer ${serverAuthData.accessToken}")
            .build()
        val response = chain.proceed(newRequest)

        return response
    }

    companion object {
        private const val AUTH_HEADER_KEY = "Authorization"
        private const val SKIP_AUTH_KEY = "Auth"
        private const val SKIP_AUTH_VALUE = "false"
    }
}
