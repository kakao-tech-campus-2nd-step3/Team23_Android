package com.kappzzang.jeongsan.login

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.model.AuthenticationResult
import com.kappzzang.jeongsan.usecase.AuthenticateWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.AuthenticateWithServerUseCase
import com.kappzzang.jeongsan.usecase.AuthorizeWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class LoginStatus { TRY_AUTOLOGIN, NOT_LOGGED_IN, IN_PROGRESS, FAILED, LOGIN_COMPLETE }
enum class KakaoLoginStatus { NOT_AVAILABLE, IDLE, ON_LOGIN, FAILED }

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val authorizeWithKakaoUseCase: AuthorizeWithKakaoUseCase,
    private val authenticateWithKakaoUseCase: AuthenticateWithKakaoUseCase,
    private val authenticateWithServerUseCase: AuthenticateWithServerUseCase,
    private val getUserInfo: GetUserInfoUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : AndroidViewModel(application) {
    private val authStatus by lazy {
        authenticateWithKakaoUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AuthenticationResult.NotLoaded
        )
    }
    private val _loginStatus = MutableStateFlow(LoginStatus.TRY_AUTOLOGIN)
    private val _kakaoLoginStatus = MutableStateFlow(KakaoLoginStatus.NOT_AVAILABLE)

    val loginStatus = _loginStatus.asStateFlow()
    val kakaoLoginStatus = _kakaoLoginStatus.asStateFlow()

    fun login() {
        viewModelScope.launch(ioDispatcher) {
            authStatus.collect { status ->
                when (status) {
                    is AuthenticationResult.NoToken -> {
                        _loginStatus.emit(LoginStatus.NOT_LOGGED_IN)
                        _kakaoLoginStatus.emit(KakaoLoginStatus.IDLE)
                    }

                    is AuthenticationResult.AuthenticationError -> {
                        handleAuthenticationError(status)
                        _loginStatus.emit(LoginStatus.FAILED)
                    }

                    is AuthenticationResult.AuthenticationSuccess -> {
                        _loginStatus.emit(LoginStatus.LOGIN_COMPLETE)
                    }

                    is AuthenticationResult.RefreshTokenExpired -> {
                        _loginStatus.emit(LoginStatus.NOT_LOGGED_IN)
                        _kakaoLoginStatus.emit(KakaoLoginStatus.IDLE)
                    }

                    is AuthenticationResult.NotLoaded -> {}
                }
            }
        }
    }

    private fun handleAuthenticationError(errorBody: AuthenticationResult.AuthenticationError) {
        Toast.makeText(application, errorBody.message, Toast.LENGTH_LONG)
            .show()
    }

    private fun mapOAuthTokenToKakaoAuthData(token: OAuthToken): KakaoAuthData = KakaoAuthData(
        kakaoAccessToken = token.accessToken,
        kakaoRefreshToken = token.refreshToken,
        accessTokenExpirationTime = token.accessTokenExpiresAt.time
    )

    fun onKakaoAuthorizationFailure(error: Throwable?) {
        error?.let {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                _kakaoLoginStatus.value = KakaoLoginStatus.IDLE
                return
            }
            if (error is AuthError && error.reason == AuthErrorCause.AccessDenied) {
                _kakaoLoginStatus.value = KakaoLoginStatus.IDLE
                return
            }
            _kakaoLoginStatus.value = KakaoLoginStatus.FAILED
        }
    }

    fun onKakaoAuthorizationSuccess(token: OAuthToken?) {
        token?.let {
            _kakaoLoginStatus.value = KakaoLoginStatus.ON_LOGIN
            viewModelScope.launch(ioDispatcher) {
                authenticateWithServer()
                authorizeWithKakao(mapOAuthTokenToKakaoAuthData(token))
            }
        }
    }

    private suspend fun authorizeWithKakao(authData: KakaoAuthData) {
        authorizeWithKakaoUseCase(authData)
        _loginStatus.emit(LoginStatus.LOGIN_COMPLETE)
    }

    private suspend fun authenticateWithServer() {
        getUserInfo()?.let {
            authenticateWithServerUseCase(it.name, it.email, it.profileUrl)
        } ?: run {
            _loginStatus.emit(LoginStatus.FAILED)
        }
    }

    fun bypassLogin() {
        _loginStatus.value = LoginStatus.LOGIN_COMPLETE
    }
}
