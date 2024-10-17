package com.kappzzang.jeongsan.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kakao.sdk.auth.TalkAuthCodeActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.kappzzang.jeongsan.login.databinding.ActivityLoginBinding
import com.kappzzang.jeongsan.navigation.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var appNavigator: AppNavigator

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setContentView(binding.root)

        binding.loginByKakaoImagebutton.setOnClickListener {
            loginWithKakao()
        }
        Log.d(TAG, intent?.data?.toString().toString())
        startCollectingKakaoLoginState()

        viewModel.login()
    }

    private fun startCollectingKakaoLoginState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginStatus.collect {
                    if (it == LoginStatus.LOGIN_COMPLETE) {
                        navigateToMainPage()
                        finish()
                    }
                }
            }
        }
    }

    private fun navigateToMainPage() {
        startActivity(appNavigator.navigateToMainPage(this))
    }

    private fun loginWithKakao() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
                viewModel.onLoginCompleteFailure(error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                viewModel.onLoginCompleteSuccess(token)
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)
                    // 사용자가 취소한 것이면 카카오 계정 로그인을 시도하지 않음
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        viewModel.onLoginCompleteFailure(error)
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡 로그인 성공 ${token.accessToken}")
                    viewModel.onLoginCompleteSuccess(token)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }
    companion object {
        private const val TAG = "LOGIN_ACTIVITY"
    }
}
