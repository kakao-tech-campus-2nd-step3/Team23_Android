package com.kappzzang.jeongsan.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kappzzang.jeongsan.data.AppLoginState
import com.kappzzang.jeongsan.data.KakaoAuthData
import com.kappzzang.jeongsan.data.ServerAuthData
import com.kappzzang.jeongsan.model.AuthenticationResult
import com.kappzzang.jeongsan.usecase.AuthenticateWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.AuthenticateWithServerUseCase
import com.kappzzang.jeongsan.usecase.AuthorizeWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.GetUserInfoUseCase
import com.kappzzang.jeongsan.usecase.LoginOrRegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authorizeWithKakaoUseCase: AuthorizeWithKakaoUseCase,
    private val authenticateWithKakaoUseCase: AuthenticateWithKakaoUseCase,
    private val authenticateWithServerUseCase: AuthenticateWithServerUseCase,
    private val loginOrRegisterUseCase: LoginOrRegisterUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val kakaoAuthStatus by lazy {
        authenticateWithKakaoUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AuthenticationResult.NotLoaded
        )
    }
    private val _appLoginStatus = MutableStateFlow<AppLoginState>(AppLoginState.TryAutoLogin)
    val appLoginStatus = _appLoginStatus.asStateFlow()

    fun clearAppLoginStatus(state: AppLoginState) {
        _appLoginStatus.value = state
    }

    fun checkKakaoTokenExist() {
        viewModelScope.launch(ioDispatcher) {
            kakaoAuthStatus.collect { result ->
                when (result) {
                    is AuthenticationResult.NoToken -> {
                        _appLoginStatus.emit(AppLoginState.Idle.NotKakaoLoggedIn)
                    }

                    is AuthenticationResult.AuthenticationError -> {
                        _appLoginStatus.emit(AppLoginState.KakaoLoginFailed(result.message))
                    }

                    is AuthenticationResult.AuthenticationSuccess -> {
                        checkServerTokenExist()
                    }

                    is AuthenticationResult.RefreshTokenExpired -> {
                        _appLoginStatus.emit(AppLoginState.Idle.NotKakaoLoggedIn)
                    }

                    is AuthenticationResult.NotLoaded -> {}
                }
            }
        }
    }

    private fun mapOAuthTokenToKakaoAuthData(token: OAuthToken): KakaoAuthData = KakaoAuthData(
        kakaoAccessToken = token.accessToken,
        kakaoRefreshToken = token.refreshToken,
        accessTokenExpirationTime = token.accessTokenExpiresAt.time
    )

    fun onKakaoAuthorizationFailure(error: Throwable?) {
        viewModelScope.launch(ioDispatcher) {
            error?.let {
                // 사용자가 취소한 경우
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    _appLoginStatus.emit(AppLoginState.Idle.NotKakaoLoggedIn)
                }
                // 사용자 동의 화면에서 카카오 로그인을 취소한 경우
                else if (error is AuthError && error.reason == AuthErrorCause.AccessDenied) {
                    _appLoginStatus.emit(AppLoginState.Idle.NotKakaoLoggedIn)
                } else {
                    Log.e(TAG, "카카오 로그인 실패", error)
                    _appLoginStatus.emit(AppLoginState.KakaoLoginFailed(error.message ?: ERROR))
                }
            }
        }
    }

    fun onKakaoAuthorizationSuccess(token: OAuthToken?) {
        token?.let {
            viewModelScope.launch(ioDispatcher) {
                authorizeWithKakao(mapOAuthTokenToKakaoAuthData(token))
            }
            loginWithServer()
        } ?: run {
            _appLoginStatus.value = AppLoginState.KakaoLoginFailed(ERROR)
        }
    }

    private suspend fun authorizeWithKakao(authData: KakaoAuthData) {
        authorizeWithKakaoUseCase(authData)
    }

    private fun isEmptyServerAuthData(authData: ServerAuthData): Boolean =
        authData.accessToken == "" || authData.refreshToken == ""

    private fun checkServerTokenExist() {
        viewModelScope.launch(ioDispatcher) {
            val serverAuthData = authenticateWithServerUseCase()

            if (isEmptyServerAuthData(serverAuthData)) {
                _appLoginStatus.emit(AppLoginState.Idle.NotServerLoggedIn)
            } else {
                _appLoginStatus.emit(AppLoginState.LoginComplete)
            }
        }
    }

    fun loginWithServer() {
        viewModelScope.launch(ioDispatcher) {
            getUserInfoUseCase()?.let {
                try {
                    loginOrRegisterUseCase(it.serviceId, it.name, it.email, it.profileUrl)
                    _appLoginStatus.emit(AppLoginState.LoginComplete)
                } catch (e: Exception) {
                    Log.e(TAG, "서버 로그인 실패", e)
                    _appLoginStatus.emit(AppLoginState.ServerLoginFailed(e.message ?: ERROR))
                }
            } ?: run {
                _appLoginStatus.emit(AppLoginState.ServerLoginFailed("유저 정보를 가져올 수 없습니다"))
            }
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
        private const val ERROR = "알 수 없는 오류가 발생했습니다"
    }
}
