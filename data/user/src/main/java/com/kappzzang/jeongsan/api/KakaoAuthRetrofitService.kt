package com.kappzzang.jeongsan.api

import com.kappzzang.jeongsan.entity.KakaoRefreshTokenPayloadDTO
import com.kappzzang.jeongsan.entity.KakaoRefreshTokenResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface KakaoAuthRetrofitService {
    @POST("/oauth/token")
    suspend fun refreshToken(
        @Header("Content-type") contentType: String,
        @Body body: KakaoRefreshTokenPayloadDTO,
        @Query("grant_type") grantType: String,
        @Query("client_id") clientId: String,
        @Query("refresh_token") refreshToken: String,
        @Query("client_secret") clientSecret: String
    ): Response<KakaoRefreshTokenResponseDTO>
}
