package com.kappzzang.jeongsan

import com.kappzzang.jeongsan.data.AuthData
import com.kappzzang.jeongsan.model.AuthenticationResult
import com.kappzzang.jeongsan.repository.KakaoAuthenticationRepository
import com.kappzzang.jeongsan.usecase.AuthenticateWithKakaoUseCase
import com.kappzzang.jeongsan.util.AuthenticationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AuthenticateWithKakaoUseCaseTest {
    private val mockAuthenticationRepository: AuthenticationRepository by lazy {
        mockk<AuthenticationRepository>(relaxed = true)
    }
    private val mockKakaoAuthenticationRepository: KakaoAuthenticationRepository by lazy {
        mockk<KakaoAuthenticationRepository>(relaxed = true)
    }

    @Test
    fun `Access 토큰이 존재하지 않으면 NoToken을 리턴한다`() {
        // given
        val emptyAuthData = AuthData(
            jwt = null,
            kakaoAccessToken = "",
            kakaoRefreshToken = "",
            accessTokenExpirationTime = 0
        )

        every { mockAuthenticationRepository.getAuthData() } returns flow { emit(emptyAuthData) }

        // when
        val useCase = AuthenticateWithKakaoUseCase(
            mockAuthenticationRepository,
            mockKakaoAuthenticationRepository
        )

        // then
        runBlocking {
            val result = useCase().last()
            assertThat(result).isEqualTo(AuthenticationResult.NoToken)
        }
    }

    @Test
    fun `만료 기한이 얼마 남지 않은 AccessToken은 Refresh하여 리턴한다`() {
        // given
        val expirationTime = System.currentTimeMillis() + 1000L * 60
        val authData = AuthData(
            jwt = null,
            kakaoAccessToken = "token",
            kakaoRefreshToken = "refresh",
            accessTokenExpirationTime = expirationTime
        )

        every { mockAuthenticationRepository.getAuthData() } returns flow { emit(authData) }

        // when
        val useCase = AuthenticateWithKakaoUseCase(
            mockAuthenticationRepository,
            mockKakaoAuthenticationRepository
        )

        // then
        runBlocking {
            val result = useCase().last()

            assertThat(result is AuthenticationResult.AuthenticationSuccess).isTrue()
            coVerify { mockKakaoAuthenticationRepository.refreshKakaoToken(any()) }
        }
    }

    @Test
    fun `AccessToken이 Refresh되면 Auth Data 또한 정상적으로 갱신된다`() {
        // given
        val newAccessToken = "newAccessToken"
        val newRefreshToken = "newRefreshToken"
        val oldJwt = "jwt"
        val newExpirationTime = System.currentTimeMillis() + 500_000L

        val authData = AuthData(
            jwt = oldJwt,
            kakaoAccessToken = "oldAccessToken",
            kakaoRefreshToken = "refreshToken",
            accessTokenExpirationTime = System.currentTimeMillis()
        )

        val newData = AuthData(
            jwt = "",
            kakaoAccessToken = newAccessToken,
            kakaoRefreshToken = newRefreshToken,
            accessTokenExpirationTime = newExpirationTime
        )

        every { mockAuthenticationRepository.getAuthData() } returns flow { emit(authData) }
        coEvery { mockKakaoAuthenticationRepository.refreshKakaoToken(any()) } returns newData

        // when
        val useCase = AuthenticateWithKakaoUseCase(
            mockAuthenticationRepository,
            mockKakaoAuthenticationRepository
        )

        // then
        runBlocking {
            val result = useCase().last()
            val resultAuthData = (result as? AuthenticationResult.AuthenticationSuccess)?.authData

            assertThat(resultAuthData).isNotNull
            assertThat(resultAuthData?.kakaoAccessToken).isEqualTo(newAccessToken)
            assertThat(resultAuthData?.kakaoRefreshToken).isEqualTo(newRefreshToken)
            assertThat(resultAuthData?.accessTokenExpirationTime).isEqualTo(newExpirationTime)
            assertThat(resultAuthData?.jwt).isEqualTo(oldJwt)
        }
    }
}
