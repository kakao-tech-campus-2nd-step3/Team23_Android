package com.kappzzang.jeongsan.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import com.kappzzang.jeongsan.login.databinding.ActivityLoginBinding
import com.kappzzang.jeongsan.navigation.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var appNavigator: AppNavigator

    private val viewModel:LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginByKakaoImagebutton.setOnClickListener {
            loginWithKakao()
        }
    }

    private fun startCollectingKakaoLoginState(){
        viewModel.loginStatus
    }

    private fun navigateToMainPage(){
        startActivity(appNavigator.navigateToMainPage(this))
    }

    private fun loginWithKakao() {
        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
            viewModel.onKakaoAuthorizationComplete(token, error)
        }
    }
}
