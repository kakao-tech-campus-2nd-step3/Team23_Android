package com.kappzzang.jeongsan.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kappzzang.jeongsan.creategroup.CreateGroupActivity
import com.kappzzang.jeongsan.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainPageViewModel by viewModels()
    private lateinit var groupListAdapter: GroupListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setGroupListRecyclerView()
        setCreateGroupButton()
        observeViewModel()
    }

    private fun setGroupListRecyclerView() {
        groupListAdapter = GroupListAdapter()
        binding.groupListRecyclerview.apply {
            adapter = groupListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setCreateGroupButton() {
        // TODO: 이후 Jetpack Navigation을 사용하여 화면 전환
        binding.createGroupButton.setOnClickListener {
            startActivity(
                Intent(this, creategroup.CreateGroupActivity::class.java)
            )
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
