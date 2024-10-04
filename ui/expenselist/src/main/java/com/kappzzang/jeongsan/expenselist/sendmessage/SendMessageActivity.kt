package com.kappzzang.jeongsan.expenselist.sendmessage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.data.Member
import com.kappzzang.jeongsan.expenselist.MemberAdapter
import com.kappzzang.jeongsan.expenselist.R
import com.kappzzang.jeongsan.expenselist.databinding.ActivitySendMessageBinding

class SendMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySendMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendMessageButton.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    com.kappzzang.jeongsan.expenselist.sendcomplete.SendCompleteActivity::class.java
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
            adapter = MemberAdapter(members.toList(), layoutInflater, R.layout.item_member)
            layoutManager = LinearLayoutManager(
                this@SendMessageActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
    }
}
