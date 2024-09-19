package com.kappzzang.jeongsan.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.databinding.ActivityMainBinding
import com.kappzzang.jeongsan.domain.model.GroupItem
import com.kappzzang.jeongsan.domain.model.GroupViewType

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
    }

    private fun createDemoGroupItemList(): List<GroupItem> = listOf(
        GroupItem(GroupViewType.PROGRESS_TITLE, "", listOf()),
        GroupItem(GroupViewType.GROUP, "캡짱모임", listOf()),
        GroupItem(GroupViewType.DONE_TITLE, "", listOf()),
        GroupItem(GroupViewType.GROUP, "모임 이름 1", listOf()),
        GroupItem(GroupViewType.GROUP, "모임 이름 2", listOf()),
        GroupItem(GroupViewType.GROUP, "모임 이름 3", listOf())
    )
}
