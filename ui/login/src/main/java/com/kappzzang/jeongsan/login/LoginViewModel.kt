package com.kappzzang.jeongsan.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kappzzang.jeongsan.model.AuthenticationResult
import com.kappzzang.jeongsan.usecase.AuthenticateWithKakaoUseCase
import com.kappzzang.jeongsan.usecase.AuthorizeWithKakaoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class LoginStatus { TRY_AUTOLOGIN, NOT_LOGGED_IN, IN_PROGRESS, FAILED, LOGIN_COMPLETE }

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val authorizeWithKakaoUseCase: AuthorizeWithKakaoUseCase,
    private val authenticateWithKakaoUseCase: AuthenticateWithKakaoUseCase
) : AndroidViewModel(application) {
    private val _loginStatus = MutableStateFlow(LoginStatus.TRY_AUTOLOGIN)
    val loginStatus = _loginStatus.asStateFlow()

    private fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = authenticateWithKakaoUseCase.invoke()) {
                is AuthenticationResult.NoToken ->
                    _loginStatus.emit(LoginStatus.NOT_LOGGED_IN)

                is AuthenticationResult.AuthenticationError ->
                    handleAuthenticationError(result)

                is AuthenticationResult.AuthenticationSuccess ->
                    _loginStatus.emit(LoginStatus.LOGIN_COMPLETE)

                is AuthenticationResult.RefreshTokenExpired ->
                    _loginStatus.emit(LoginStatus.LOGIN_COMPLETE)
            }
        }
    }

    private fun handleAuthenticationError(errorBody: AuthenticationResult.AuthenticationError) {

    }

    fun authorizeWithKakao() {

    }
}