package com.kappzzang.jeongsan.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kakao.sdk.user.UserApiClient
import com.kappzzang.jeongsan.login.databinding.ActivityLoginBinding
import com.kappzzang.jeongsan.navigation.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var appNavigator: AppNavigator

    private val viewModel:LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setContentView(binding.root)

        binding.loginByKakaoImagebutton.setOnClickListener {
            loginWithKakao()
        }

        startCollectingKakaoLoginState()

        viewModel.login()
    }

    private fun startCollectingKakaoLoginState(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginStatus.collect{
                    if(it == LoginStatus.LOGIN_COMPLETE){
                        navigateToMainPage()
                    }
                }
            }
        }
    }

    private fun navigateToMainPage(){
        startActivity(appNavigator.navigateToMainPage(this))
    }

    private fun loginWithKakao() {
        if(!UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
            Toast.makeText(this, R.string.login_kakao_talk_not_available, Toast.LENGTH_SHORT).show()
            return
        }
        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
            viewModel.onKakaoAuthorizationComplete(token, error)
        }
    }
}
