package com.kappzzang.jeongsan.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kappzzang.jeongsan.data.GroupViewItem
import com.kappzzang.jeongsan.intentcontract.ExpenseListContract
import com.kappzzang.jeongsan.main.databinding.ActivityMainBinding
import com.kappzzang.jeongsan.navigation.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    private lateinit var appNavigator: AppNavigator

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
        groupListAdapter = GroupListAdapter { id ->
            val intent = appNavigator.navigateToExpenseList(this)
            intent.putExtra(ExpenseListContract.GROUP_ID, id)
            ContextCompat.startActivity(binding.root.context, intent, null)
        }

        binding.groupListRecyclerview.apply {
            adapter = groupListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setCreateGroupButton() {
        // TODO: 이후 Jetpack Navigation을 사용하여 화면 전환
        binding.createGroupButton.setOnClickListener {
            startActivity(
                appNavigator.navigateToCreateGroup(this)
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
