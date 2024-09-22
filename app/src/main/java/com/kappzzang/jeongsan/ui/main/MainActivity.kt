package com.kappzzang.jeongsan.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kappzzang.jeongsan.databinding.ActivityMainBinding
import com.kappzzang.jeongsan.ui.creategroup.CreateGroupActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: GroupInfoViewModel by viewModels()
    private lateinit var groupListAdapter: GroupListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setGroupListRecyclerView()
        observeViewModel()

        // TODO: 임시 연결용 코드
        binding.createGroupButton.setOnClickListener {
            startActivity(Intent(this, CreateGroupActivity::class.java))
        }
    }

    private fun setGroupListRecyclerView() {
        groupListAdapter = GroupListAdapter()
        binding.groupListRecyclerview.apply {
            adapter = groupListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.groupList.collect { groupList ->
                groupListAdapter.submitList(groupList)
            }
        }

        lifecycleScope.launch {
            viewModel.userProfileUrl.collect { userProfileUrl ->
                Glide.with(this@MainActivity)
                    .load(userProfileUrl)
                    .circleCrop()
                    .into(binding.profileImageImageview)
            }
        }
    }
}
