package com.kappzzang.jeongsan.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kappzzang.jeongsan.login.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginByKakaoImagebutton.setOnClickListener {
            // TODO: 카카오 로그인 구현하기
            // startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
