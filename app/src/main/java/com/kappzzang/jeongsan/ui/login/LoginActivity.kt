package com.kappzzang.jeongsan.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kappzzang.jeongsan.databinding.ActivityLoginBinding
import com.kappzzang.jeongsan.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginByKakaoImagebutton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}