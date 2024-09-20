package com.kappzzang.jeongsan.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.databinding.ActivityMainBinding
import com.kappzzang.jeongsan.ui.creategroup.CreateGroupActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 임시 ui 코드
        binding.userNameTextview.text = "라이언"

        binding.groupListRecyclerview.adapter = GroupListAdapter(createDemoGroupItemList())
        binding.groupListRecyclerview.layoutManager = LinearLayoutManager(this)

        // TODO: 임시 연결용 코드
        binding.createGroupButton.setOnClickListener {
            startActivity(Intent(this, CreateGroupActivity::class.java))
        }
    }

    private fun createDemoGroupItemList(): List<GroupViewItem> = listOf(
        GroupViewItem("0", "", false, listOf(), GroupViewType.PROGRESS_TITLE),
        GroupViewItem("1", "캡짱모임", false, listOf(), GroupViewType.GROUP),
        GroupViewItem("2", "", false, listOf(), GroupViewType.DONE_TITLE),
        GroupViewItem("3", "모임 이름 1", true, listOf(), GroupViewType.GROUP),
        GroupViewItem("4", "모임 이름 2", true, listOf(), GroupViewType.GROUP),
        GroupViewItem("5", "모임 이름 3", true, listOf(), GroupViewType.GROUP)
    )
}
