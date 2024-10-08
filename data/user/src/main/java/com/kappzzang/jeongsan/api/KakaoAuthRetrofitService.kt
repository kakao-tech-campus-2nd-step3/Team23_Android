package com.kappzzang.jeongsan.api

import com.kappzzang.jeongsan.entity.KakaoRefreshTokenPayloadDTO
import com.kappzzang.jeongsan.entity.KakaoRefreshTokenResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface KakaoAuthRetrofitService {
    @POST("/oauth/token")
    suspend fun refreshToken(
        @Header("Content-type") contentType: String,
        @Body body: KakaoRefreshTokenPayloadDTO
    ): Response<KakaoRefreshTokenResponseDTO>
}