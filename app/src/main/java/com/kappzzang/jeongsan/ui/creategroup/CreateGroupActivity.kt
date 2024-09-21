package com.kappzzang.jeongsan.ui.creategroup

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.R
import com.kappzzang.jeongsan.databinding.ActivityCreateGroupBinding
import com.kappzzang.jeongsan.domain.model.MemberItem
import com.kappzzang.jeongsan.ui.Member
import com.kappzzang.jeongsan.ui.MemberAdapter

class CreateGroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val members = mutableListOf<Member>()
        for (i in 0..10) {
            members.add(
                Member("Member$i")
            )
        }

        val category = resources.getStringArray(R.array.group_category)

        binding.memberContentRecyclerview.adapter =
            MemberAdapter(members.toList(), layoutInflater, R.layout.item_member_invite)
        binding.memberContentRecyclerview.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.groupCategoryContentSpinner.apply {
            adapter = ArrayAdapter(
                this@CreateGroupActivity,
                android.R.layout.simple_spinner_dropdown_item,
                category
            )
        }

        // TODO: 임시 연결용 코드
        binding.createGroupButton.setOnClickListener {
            finish()
        }
    }
}
