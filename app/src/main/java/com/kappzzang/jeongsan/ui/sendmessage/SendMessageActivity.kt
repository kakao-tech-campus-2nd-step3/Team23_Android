package com.kappzzang.jeongsan.ui.sendmessage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.R
import com.kappzzang.jeongsan.databinding.ActivitySendMessageBinding
import com.kappzzang.jeongsan.ui.Member
import com.kappzzang.jeongsan.ui.MemberAdapter
import com.kappzzang.jeongsan.ui.sendcomplete.SendCompleteActivity

class SendMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySendMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendMessageButton.setOnClickListener {
            startActivity(Intent(this, SendCompleteActivity::class.java))
        }

        val members = mutableListOf<Member>()
        for (i in 0..3) {
            members.add(
                Member("Member$i")
            )
        }
        binding.infoContentRecyclerview.apply {
            adapter = MemberAdapter(members.toList(), layoutInflater, R.layout.activity_main)
            layoutManager = LinearLayoutManager(
                this@SendMessageActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
    }
}
