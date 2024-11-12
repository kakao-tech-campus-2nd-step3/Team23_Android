package com.kappzzang.jeongsan.login

import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.AuthErrorResponse
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kappzzang.jeongsan.data.AppLoginState
import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.model.AuthenticationResult
import com.kappzzang.jeongsan.model.UserItem
import com.kappzzang.jeongsan.usecase.AuthenticateWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.AuthenticateWithServerUseCase
import com.kappzzang.jeongsan.usecase.AuthorizeWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.GetUserInfoUseCase
import com.kappzzang.jeongsan.usecase.LoginOrRegisterUseCase
import com.kappzzang.jeongsan.usecase.LoginWithTestAccountUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {
    private val authorizeWithKakaoUseCase = mockk<AuthorizeWithKakaoUseCase>(relaxed = true)
    private val authenticateWithKakaoUseCase = mockk<AuthenticateWithKakaoUseCase>()
    private val authenticationWithServerUseCase =
        mockk<AuthenticateWithServerUseCase>(relaxed = true)
    private val loginOrRegisterUseCase = mockk<LoginOrRegisterUseCase>(relaxed = true)
    private val loginWithTestAccountUseCase = mockk<LoginWithTestAccountUseCase>(relaxed = true)
    private val getUserInfoUseCase = mockk<GetUserInfoUseCase>()
    private lateinit var viewModel: LoginViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        viewModel = LoginViewModel(
            authorizeWithKakaoUseCase,
            authenticateWithKakaoUseCase,
            authenticationWithServerUseCase,
            loginOrRegisterUseCase,
            loginWithTestAccountUseCase,
            getUserInfoUseCase,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `카카오 인증 실패 - ClientError Cancelled이면 AppLoginState는 NotKakaoLoggedIn으로 설정되어야 한다`() =
        runTest {
            // given
            val error = ClientError(ClientErrorCause.Cancelled)

            // when
            viewModel.onKakaoAuthorizationFailure(error)
            advanceUntilIdle()

            // then
            assertEquals(AppLoginState.Idle.NotKakaoLoggedIn, viewModel.appLoginStatus.value)
        }

    @Test
    fun `카카오 인증 실패 - AuthError AccessDenied이면 AppLoginState는 IDLE로 설정`() = runTest {
        // given
        val responseCode = 401 // 예시로 적절한 HTTP 응답 코드를 설정
        val errorResponse = AuthErrorResponse("테스트 Auth 에러", "테스트")
        val error = AuthError(responseCode, AuthErrorCause.AccessDenied, errorResponse)

        // when
        viewModel.onKakaoAuthorizationFailure(error)
        advanceUntilIdle()

        // then
        assertEquals(AppLoginState.Idle.NotKakaoLoggedIn, viewModel.appLoginStatus.value)
    }

    // 고치기
    @Test
    fun `카카오 인증 실패 - 그 외의 오류가 발생하면 AppLoginState는 KakaoLoginFailed로 설정`() = runTest {
        // given
        val testError = Throwable("Some other error")

        // when
        viewModel.onKakaoAuthorizationFailure(testError)
        advanceUntilIdle()

        // then
        assertEquals(
            AppLoginState.KakaoLoginFailed(testError.message ?: "Unknown error"),
            viewModel.appLoginStatus.value
        )
    }

    @Test
    fun `카카오 인증 성공 - 유효한 OAuthToken이면 토큰을 저장하고 서버 로그인 시도`() = runTest {
        // given
        val token = mockk<OAuthToken>()
        every { token.accessToken } returns "valid_token"
        every { token.refreshToken } returns "valid_refresh_token"
        val testDate = Date()
        every { token.accessTokenExpiresAt } returns testDate
        val testUserItem = UserItem(
            serviceId = "test-service-id",
            name = "test-user",
            email = "test@test.test",
            profileUrl = "this-is-test-url"
        )
        coEvery { getUserInfoUseCase() } returns testUserItem
        coEvery { loginOrRegisterUseCase(any(), any(), any(), any()) } returns Unit

        // when
        viewModel.onKakaoAuthorizationSuccess(token)
        advanceUntilIdle()

        // then
        coVerify {
            authorizeWithKakaoUseCase(
                KakaoAuthData(
                    kakaoAccessToken = "valid_token",
                    kakaoRefreshToken = "valid_refresh_token",
                    accessTokenExpirationTime = testDate.time
                )
            )
        }
        coVerify {
            loginOrRegisterUseCase(
                testUserItem.serviceId,
                testUserItem.name,
                testUserItem.email,
                testUserItem.profileUrl
            )
        }
    }

    @Test
    fun `login 시 카카오 토큰의 NoToken의 상태 반영`() = runTest {
        // given
        coEvery { authenticateWithKakaoUseCase() } returns flowOf(
            AuthenticationResult.NoToken
        )

        // when
        viewModel.checkKakaoTokenExist()
        advanceUntilIdle()

        // then
        assertEquals(AppLoginState.Idle.NotKakaoLoggedIn, viewModel.appLoginStatus.value)
    }

    @Test
    fun `login 시 카카오 토큰의 AuthenticationError의 상태 반영`() = runTest {
        // given
        val errorMessage = "Error message"
        coEvery { authenticateWithKakaoUseCase() } returns flowOf(
            AuthenticationResult.AuthenticationError(errorMessage)
        )

        // when
        viewModel.checkKakaoTokenExist()
        advanceUntilIdle()

        assertEquals(AppLoginState.KakaoLoginFailed(errorMessage), viewModel.appLoginStatus.value)
    }

    @Test
    fun `login 시 카카오 토큰의 RefreshTokenExpired 의 상태 반영`() = runTest {
        // given
        coEvery { authenticateWithKakaoUseCase() } returns flowOf(
            AuthenticationResult.RefreshTokenExpired
        )

        // when
        viewModel.checkKakaoTokenExist()
        advanceUntilIdle()

        // then
        assertEquals(AppLoginState.Idle.NotKakaoLoggedIn, viewModel.appLoginStatus.value)
    }
}
