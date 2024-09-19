package com.kappzzang.jeongsan.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kappzzang.jeongsan.databinding.ActivityHistoryBinding
import com.kappzzang.jeongsan.ui.inviteinfo.InviteInfoActivity
import com.kappzzang.jeongsan.ui.sendmessage.SendMessageActivity

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tempInviteInfoButton.setOnClickListener {
            startActivity(Intent(this, InviteInfoActivity::class.java))
        }
        binding.tempSendMessageButton.setOnClickListener {
            startActivity(Intent(this, SendMessageActivity::class.java))
        }
    }
}