package com.kappzzang.jeongsan.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.databinding.ActivityMainBinding
import com.kappzzang.jeongsan.domain.model.GroupItem
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
        GroupViewItem.ProgressTitle,
        GroupViewItem.Group(GroupItem("1", "캡짱모임", false, listOf(TEST_IMAGE))),
        GroupViewItem.DoneTitle,
        GroupViewItem.Group(GroupItem("2", "모임 1", true, listOf(TEST_IMAGE))),
        GroupViewItem.Group(GroupItem("3", "모임 2", true, listOf(TEST_IMAGE, TEST_IMAGE))),
        GroupViewItem.Group(
            GroupItem(
                "4",
                "모임 3",
                true,
                listOf(TEST_IMAGE, TEST_IMAGE, TEST_IMAGE)
            )
        )
    )

    companion object {
        private const val TEST_IMAGE = "https://www.studiopeople.kr/common/img/default_profile.png"
    }
}
