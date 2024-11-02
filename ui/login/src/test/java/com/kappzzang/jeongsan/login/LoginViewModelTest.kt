package com.kappzzang.jeongsan.login

import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.AuthErrorResponse
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kappzzang.jeongsan.model.AuthenticationResult
import com.kappzzang.jeongsan.usecase.AuthenticateWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.AuthorizeWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.RegisterWithKakaoUseCase
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
    private val authorizeWithKakaoUseCase = mockk<AuthorizeWithKakaoUseCase>()
    private val authenticateWithKakaoUseCase = mockk<AuthenticateWithKakaoUseCase>()
    private val registerUseCase = mockk<RegisterWithKakaoUseCase>()
    private lateinit var viewModel: LoginViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Toast::class)
        every { Toast.makeText(any(), any<String>(), any()) } returns mockk {
            every { show() } returns Unit
        }
        viewModel = LoginViewModel(
            mockk(),
            authorizeWithKakaoUseCase,
            authenticateWithKakaoUseCase,
            registerUseCase,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
        Dispatchers.resetMain()
    }

    @Test
    fun `카카오 인증 실패 - ClientError Cancelled이면 KakaoLoginStatus는 IDLE로 설정되어야 한다`() = runTest {
        val error = ClientError(ClientErrorCause.Cancelled)

        viewModel.onKakaoAuthorizationFailure(error)

        assertEquals(KakaoLoginStatus.IDLE, viewModel.kakaoLoginStatus.value)
    }

    @Test
    fun `카카오 인증 실패 - AuthError AccessDenied이면 KakaoLoginStatus는 IDLE로 설정`() = runTest {
        val responseCode = 401 // 예시로 적절한 HTTP 응답 코드를 설정
        val errorResponse = AuthErrorResponse("테스트 Auth 에러", "테스트")
        val error = AuthError(responseCode, AuthErrorCause.AccessDenied, errorResponse)

        viewModel.onKakaoAuthorizationFailure(error)

        assertEquals(KakaoLoginStatus.IDLE, viewModel.kakaoLoginStatus.value)
    }

    @Test
    fun `카카오 인증 실패 - 그 외의 오류가 발생하면 KakaoLoginStatus는 FAILED로 설정`() = runTest {
        val error = Throwable("Some other error")

        viewModel.onKakaoAuthorizationFailure(error)

        assertEquals(KakaoLoginStatus.FAILED, viewModel.kakaoLoginStatus.value)
    }

    @Test
    fun `카카오 인증 성공 - 유효한 OAuthToken이면 KakaoLoginStatus는 ON_LOGIN으로 설정되고 authorizeWithKakao가 호출`() =
        runTest {
            val token = mockk<OAuthToken>()
            every { token.accessToken } returns "valid_token"
            every { token.refreshToken } returns "valid_refresh_token"
            every { token.accessTokenExpiresAt } returns Date()
            coEvery { authorizeWithKakaoUseCase(any()) } returns Unit

            viewModel.onKakaoAuthorizationSuccess(token)
            advanceUntilIdle()

            coVerify { authorizeWithKakaoUseCase(any()) }
            assertEquals(KakaoLoginStatus.ON_LOGIN, viewModel.kakaoLoginStatus.value)
        }

    @Test
    fun `login 시 NoToken의 상태 반영`() = runTest {
        coEvery { authenticateWithKakaoUseCase() } returns flowOf(
            AuthenticationResult.NoToken
        )

        viewModel.login()
        advanceUntilIdle()

        assertEquals(LoginStatus.NOT_LOGGED_IN, viewModel.loginStatus.value)
        assertEquals(KakaoLoginStatus.IDLE, viewModel.kakaoLoginStatus.value)
    }

    @Test
    fun `login 시 AuthenticationError의 상태 반영`() = runTest {
        coEvery { authenticateWithKakaoUseCase() } returns flowOf(
            AuthenticationResult.AuthenticationError("Error")
        )

        viewModel.login()
        advanceUntilIdle()

        assertEquals(LoginStatus.FAILED, viewModel.loginStatus.value)
    }

    @Test
    fun `login 시 AuthenticationSuccess의 상태 반영`() = runTest {
        coEvery { authenticateWithKakaoUseCase() } returns flowOf(
            AuthenticationResult.AuthenticationSuccess(mockk())
        )

        viewModel.login()
        advanceUntilIdle()

        assertEquals(LoginStatus.LOGIN_COMPLETE, viewModel.loginStatus.value)
    }

    @Test
    fun `login 시 RefreshTokenExpired 의 상태 반영`() = runTest {
        coEvery { authenticateWithKakaoUseCase() } returns flowOf(
            AuthenticationResult.RefreshTokenExpired
        )

        viewModel.login()
        advanceUntilIdle()

        assertEquals(LoginStatus.NOT_LOGGED_IN, viewModel.loginStatus.value)
        assertEquals(KakaoLoginStatus.IDLE, viewModel.kakaoLoginStatus.value)
    }
}
