package com.kappzzang.jeongsan.ui.inviteinfo

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.R
import com.kappzzang.jeongsan.databinding.ActivityInviteInfoBinding
import com.kappzzang.jeongsan.ui.Member
import com.kappzzang.jeongsan.ui.MemberAdapter

class InviteInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInviteInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dm = applicationContext.resources.displayMetrics
        val width = (dm.widthPixels * 0.9).toInt()
        val height = (dm.heightPixels * 0.9).toInt()

        window.attributes.apply {
            this.width = width
            this.height = height
        }

        binding.closeImageview.setOnClickListener {
            finish()
        }

        val members = mutableListOf<Member>()
        for (i in 0..10) {
            members.add(
                Member("Member$i")
            )
        }

        binding.memberContentRecyclerview.adapter = MemberAdapter(members.toList(), layoutInflater, R.layout.item_member_info)
        binding.memberContentRecyclerview.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
    }
}