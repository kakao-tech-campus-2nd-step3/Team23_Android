package com.kappzzang.jeongsan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kappzzang.jeongsan.navigation.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    @Inject
    lateinit var appNavigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: 임시로 바로 로그인 페이지로 이동
        // 이후 로그인 정보를 통해 바로 메인 페이지로 보내거나 결정하는 로직
        appNavigator.navigateToLogin(this).also {
            startActivity(it)
            finish()
        }
    }
}
