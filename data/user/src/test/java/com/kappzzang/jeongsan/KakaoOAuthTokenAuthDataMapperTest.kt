package com.kappzzang.jeongsan

import com.kappzzang.jeongsan.data.AuthData
import com.kappzzang.jeongsan.entity.KakaoRefreshTokenResponseDTO
import com.kappzzang.jeongsan.mapper.KakaoOAuthTokenAuthDataMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class KakaoOAuthTokenAuthDataMapperTest {
    private fun getSampleDTO() = KakaoRefreshTokenResponseDTO(
        refreshToken = null,
        tokenType = "type",
        accessToken = "accessToken",
        accessTokenExpiresInSeconds = 10,
        refreshTokenExpiresInSeconds = 10
    )

    @Test
    fun `Null 데이터에 매핑하면 AccessToken 및 RefreshToken이 정상적으로 담긴다`() {
        // given
        val refreshToken = "refresh"
        val accessToken = "access"
        val responseDto = getSampleDTO().copy(
            refreshToken = refreshToken,
            accessToken = accessToken
        )

        // when
        val mapped = KakaoOAuthTokenAuthDataMapper.mapRefreshDtoToAuthData(responseDto, null)

        // then
        assertThat(mapped.kakaoRefreshToken).isEqualTo(refreshToken)
        assertThat(mapped.kakaoAccessToken).isEqualTo(accessToken)
    }

    @Test
    fun `이미 존재하는 데이터에 매핑하면 AccessToken 및 ExpirationTime이 정상적으로 갱신된다`() {
        // given
        val newAccessToken = "newAccessToken"
        val outdatedExpirationTime = 7L
        val responseDto = getSampleDTO().copy(
            accessToken = newAccessToken
        )
        val authData = AuthData(
            kakaoAccessToken = "oldAccessToken",
            kakaoRefreshToken = "refreshToken",
            accessTokenExpirationTime = outdatedExpirationTime,
            jwt = "jwt"
        )

        // when
        val mapped = KakaoOAuthTokenAuthDataMapper.mapRefreshDtoToAuthData(responseDto, authData)

        // then
        assertThat(mapped.kakaoAccessToken).isEqualTo(newAccessToken)
        assertThat(mapped.accessTokenExpirationTime).isNotEqualTo(outdatedExpirationTime)
        assertThat(mapped.accessTokenExpirationTime).isNotEqualTo(0)
    }

    @Test
    fun `이미 존재하는 데이터에 매핑하면 RefreshToken 및 JWT가 정상적으로 유지된다`() {
        // given
        val refreshToken = "refresh"
        val jwt = "jwt"
        val authData = AuthData(
            kakaoAccessToken = "",
            kakaoRefreshToken = refreshToken,
            accessTokenExpirationTime = 0,
            jwt = jwt
        )
        val responseDTO = getSampleDTO()

        // when
        val mapped = KakaoOAuthTokenAuthDataMapper.mapRefreshDtoToAuthData(responseDTO, authData)

        // then
        assertThat(mapped.jwt).isEqualTo(jwt)
        assertThat(mapped.kakaoRefreshToken).isEqualTo(refreshToken)
    }

    @Test
    fun `데이터가 이미 존재하더라도 RefreshToken이 재발급되면 정상적으로 갱신된다`() {
        // given
        val refreshToken = "refresh"
        val newRefreshToken = "newRefresh"

        val authData = AuthData(
            kakaoAccessToken = "",
            kakaoRefreshToken = refreshToken,
            accessTokenExpirationTime = 0,
            jwt = "jwt"
        )
        val responseDTO = getSampleDTO().copy(
            refreshToken = newRefreshToken
        )

        // when
        val mapped = KakaoOAuthTokenAuthDataMapper.mapRefreshDtoToAuthData(responseDTO, authData)

        // then
        assertThat(mapped.kakaoRefreshToken).isEqualTo(newRefreshToken)
    }
}
