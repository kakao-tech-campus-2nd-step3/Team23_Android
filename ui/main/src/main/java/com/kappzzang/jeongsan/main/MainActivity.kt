package com.kappzzang.jeongsan.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kappzzang.jeongsan.intentcontract.ExpenseListContract
import com.kappzzang.jeongsan.main.databinding.ActivityMainBinding
import com.kappzzang.jeongsan.navigation.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var appNavigator: AppNavigator

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

    private fun checkIsInvited() {
        if (intent?.data != null) {
            val inviteGroupId = intent.data.toString()
            if (!viewModel.isAlreadyJoined(inviteGroupId)) {
                showJoinGroupDialog(intent.data.toString())
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.main_already_joined),
                    Toast.LENGTH_SHORT
                ).show()
            }
            intent.data = null
        }
    }

    private fun showJoinGroupDialog(groupId: String) {
        // TODO: 그룹 아이디를 통해 그룹명 획득

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.main_want_join))
        // TODO: 가입 하기
        builder.setPositiveButton(getString(R.string.main_positive_response)) { dialog, which ->
        }
        // TODO: 거절하기
        builder.setNegativeButton(getString(R.string.main_negative_response)) { dialog, which ->
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadGroupList()
        checkIsInvited()
    }

    companion object {
        private const val TAG = "MAIN_ACTIVITY"
    }
}
