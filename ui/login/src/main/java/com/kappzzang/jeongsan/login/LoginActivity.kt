package com.kappzzang.jeongsan.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kappzzang.jeongsan.build_config.BuildConfig
import com.kappzzang.jeongsan.data.AppLoginState
import com.kappzzang.jeongsan.login.databinding.ActivityLoginBinding
import com.kappzzang.jeongsan.navigation.MainPageNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var appNavigator: MainPageNavigator

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)

        collectAppLoginState()
        createBypassLogin()
    }

    private fun collectAppLoginState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appLoginStatus.collect { state ->
                    Log.d(TAG, "AppLoginState: $state")
                    when (state) {
                        // 자동 로그인 시도
                        is AppLoginState.TryAutoLogin -> {
                            viewModel.checkKakaoTokenExist()
                        }

                        // 카카오 토큰이 없어서 로그인이 필요한 상태
                        is AppLoginState.Idle.NotKakaoLoggedIn -> {
                            binding.loginByKakaoImagebutton.setOnClickListener {
                                loginWithKakao()
                            }
                        }

                        // 서버 토큰이 없어서 로그인이 필요한 상태
                        is AppLoginState.Idle.NotServerLoggedIn -> {
                            binding.loginByKakaoImagebutton.setOnClickListener {
                                viewModel.loginWithServer()
                            }
                        }

                        // 카카오 로그인 실패
                        is AppLoginState.KakaoLoginFailed -> {
                            Log.d(TAG, "카카오 로그인 실패: ${state.message}")
                            Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT)
                                .show()
                            viewModel.clearAppLoginStatus(AppLoginState.Idle.NotKakaoLoggedIn)
                        }

                        // 서버 로그인 실패
                        is AppLoginState.ServerLoginFailed -> {
                            Log.d(TAG, "서버 로그인 실패: ${state.message}")
                            Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT)
                                .show()
                            viewModel.clearAppLoginStatus(AppLoginState.Idle.NotServerLoggedIn)
                        }

                        // 모든 로그인 완료
                        is AppLoginState.LoginComplete -> {
                            navigateToMainPage()
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun navigateToMainPage() {
        val intent = if (intent.data == null) {
            appNavigator.navigateToMainPage(this)
        } else {
            appNavigator.navigateToMainPageAndWithUri(this, intent.data!!)
        }
        startActivity(intent)
    }

    private fun loginWithKakao() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
                viewModel.onKakaoAuthorizationFailure(error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                viewModel.onKakaoAuthorizationSuccess(token)
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)
                    // 사용자가 취소한 것이면 카카오 계정 로그인을 시도하지 않음
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        viewModel.onKakaoAuthorizationFailure(error)
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡 로그인 성공 ${token.accessToken}")
                    viewModel.onKakaoAuthorizationSuccess(token)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun createBypassLogin() {
        if (BuildConfig.DEBUG) {
            binding.loginByKakaoImagebutton.isLongClickable = true
            binding.loginByKakaoImagebutton.setOnLongClickListener {
                viewModel.bypassLogin()
                true
            }
        }
    }

    companion object {
        private const val TAG = "LOGIN_ACTIVITY"
    }
}
