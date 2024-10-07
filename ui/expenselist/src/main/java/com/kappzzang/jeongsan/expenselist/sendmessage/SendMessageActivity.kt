package com.kappzzang.jeongsan.expenselist.sendmessage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.data.Member
import com.kappzzang.jeongsan.expenselist.MemberAdapter
import com.kappzzang.jeongsan.expenselist.R
import com.kappzzang.jeongsan.expenselist.databinding.ActivitySendMessageBinding
import com.kappzzang.jeongsan.expenselist.sendcomplete.SendCompleteActivity

class SendMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySendMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Dialog로 대체
        binding.sendMessageButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SendCompleteActivity::class.java
                )
            )
        }

        val members = mutableListOf<Member>()
        for (i in 0..3) {
            members.add(
                Member("Member$i")
            )
        }
        binding.infoContentRecyclerview.apply {
            adapter = MemberAdapter(
                members.toList(),
                layoutInflater,
                com.kappzzang.jeongsan.R.layout.item_member
            )
            layoutManager = LinearLayoutManager(
                this@SendMessageActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
    }
}
