package com.kappzzang.jeongsan.login

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kappzzang.jeongsan.data.AuthData
import com.kappzzang.jeongsan.model.AuthenticationResult
import com.kappzzang.jeongsan.usecase.AuthenticateWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.AuthorizeWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.RegisterWithKakaoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class LoginStatus { TRY_AUTOLOGIN, NOT_LOGGED_IN, IN_PROGRESS, FAILED, LOGIN_COMPLETE }
enum class KakaoLoginStatus { NOT_AVAILABLE, IDLE, ON_LOGIN, FAILED }

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val authorizeWithKakaoUseCase: AuthorizeWithKakaoUseCase,
    private val authenticateWithKakaoUseCase: AuthenticateWithKakaoUseCase,
    private val registerUseCase: RegisterWithKakaoUseCase
) : AndroidViewModel(application) {
    private val _loginStatus = MutableStateFlow(LoginStatus.TRY_AUTOLOGIN)
    private val _kakaoLoginStatus = MutableStateFlow(KakaoLoginStatus.NOT_AVAILABLE)

    val loginStatus = _loginStatus.asStateFlow()
    val kakaoLoginStatus = _kakaoLoginStatus.asStateFlow()

    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            _loginStatus.emit(LoginStatus.IN_PROGRESS)
            when (val result = authenticateWithKakaoUseCase()) {
                is AuthenticationResult.NoToken -> {
                    _loginStatus.emit(LoginStatus.NOT_LOGGED_IN)
                    _kakaoLoginStatus.emit(KakaoLoginStatus.IDLE)
                }

                is AuthenticationResult.AuthenticationError -> {
                    handleAuthenticationError(result)
                    _loginStatus.emit(LoginStatus.FAILED)
                }

                is AuthenticationResult.AuthenticationSuccess ->
                    _loginStatus.emit(LoginStatus.LOGIN_COMPLETE)

                is AuthenticationResult.RefreshTokenExpired -> {
                    _loginStatus.emit(LoginStatus.NOT_LOGGED_IN)
                    _kakaoLoginStatus.emit(KakaoLoginStatus.IDLE)
                }
            }
        }
    }

    private fun handleAuthenticationError(errorBody: AuthenticationResult.AuthenticationError) {
        Toast.makeText(application, errorBody.message, Toast.LENGTH_LONG)
            .show()
    }

    private fun mapOAuthTokenToAuthData(token: OAuthToken): AuthData = AuthData(
        kakaoAccessToken = token.accessToken,
        kakaoRefreshToken = token.refreshToken,
        accessTokenExpirationTime = token.accessTokenExpiresAt.time,
        jwt = null
    )

    fun onKakaoAuthorizationComplete(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                _kakaoLoginStatus.value = KakaoLoginStatus.IDLE
                return
            }

            _kakaoLoginStatus.value = KakaoLoginStatus.FAILED
        }
        else if(token != null){
            _kakaoLoginStatus.value = KakaoLoginStatus.ON_LOGIN
            authorizeWithKakao(mapOAuthTokenToAuthData(token))
        }
    }

    private fun authorizeWithKakao(authData: AuthData) {
        viewModelScope.launch(Dispatchers.IO) {
            authorizeWithKakaoUseCase(authData)
            _loginStatus.emit(LoginStatus.LOGIN_COMPLETE)
        }
    }
}