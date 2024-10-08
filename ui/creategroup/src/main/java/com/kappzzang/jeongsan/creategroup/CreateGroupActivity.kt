package com.kappzzang.jeongsan.creategroup

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.creategroup.databinding.ActivityCreateGroupBinding
import com.kappzzang.jeongsan.data.Member

class CreateGroupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSpinner()

        val members = mutableListOf<Member>()
        for (i in 0..10) {
            members.add(
                Member("Member$i")
            )
        }

        binding.memberContentRecyclerview.adapter =
            MemberAdapter(
                members.toList(),
                layoutInflater,
                R.layout.item_member_invite
            )

        binding.memberContentRecyclerview.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        // TODO: 임시 연결용 코드
        binding.createGroupButton.setOnClickListener {
            finish()
        }
    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            this@CreateGroupActivity,
            R.array.create_group_category_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.groupCategoryContentSpinner.adapter = adapter
            binding.groupCategoryContentSpinner.setSelection(adapter.count - 1)
        }
    }
}
