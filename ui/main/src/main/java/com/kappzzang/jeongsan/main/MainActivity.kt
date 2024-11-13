package com.kappzzang.jeongsan.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.kappzzang.jeongsan.data.JoinGroupUIState
import com.kappzzang.jeongsan.intentcontract.ExpenseListContract
import com.kappzzang.jeongsan.main.databinding.ActivityMainBinding
import com.kappzzang.jeongsan.navigation.CreateGroupNavigator
import com.kappzzang.jeongsan.navigation.ExpenseListNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var expenseListNavigator: ExpenseListNavigator

    @Inject
    lateinit var createGroupNavigator: CreateGroupNavigator

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
        setSwipeRefresh()
        collectJoinGroupState()
        checkIntentHaveUri()
    }

    private fun setGroupListRecyclerView() {
        groupListAdapter = GroupListAdapter(
            { id ->
                val intent = expenseListNavigator.navigateToExpenseList(this, id)
                intent.putExtra(ExpenseListContract.GROUP_ID, id)
                ContextCompat.startActivity(binding.root.context, intent, null)
            },
            viewModel::toggleProgressGroup,
            viewModel::toggleDoneGroup
        )

        binding.groupListRecyclerview.apply {
            adapter = groupListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setCreateGroupButton() {
        binding.createGroupButton.setOnClickListener {
            startActivity(
                createGroupNavigator.navigateToCreateGroup(this)
            )
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupList.collect { groupList ->
                    groupListAdapter.submitList(groupList)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userProfileUrl.collect { userProfileUrl ->
                    Glide.with(this@MainActivity)
                        .load(userProfileUrl)
                        .circleCrop()
                        .into(binding.profileImageImageview)
                }
            }
        }
    }

    private fun showJoinGroupDialog(groupId: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.main_want_join))
        builder.setPositiveButton(getString(R.string.main_positive_response)) { _, _ ->
            viewModel.joinGroup(groupId)
        }
        builder.setNegativeButton(getString(R.string.main_negative_response)) { _, _ ->
            // Do nothing
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun collectJoinGroupState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.joinGroupState.collect { state ->
                    when (state) {
                        is JoinGroupUIState.Idle -> {}
                        is JoinGroupUIState.Success -> {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.main_join_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.clearJoinGroupState()

                            startActivity(
                                expenseListNavigator.navigateToExpenseList(
                                    this@MainActivity,
                                    state.groupId
                                )
                            )
                        }

                        is JoinGroupUIState.Error -> {
                            Toast.makeText(
                                this@MainActivity,
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.clearJoinGroupState()
                        }
                    }
                }
            }
        }
    }

    private fun checkIntentHaveUri() {
        val uri = intent.data ?: return
        when (uri.host) {
            // 모임 초대 링크를 통해 들어온 경우
            "inviteGroup" -> {
                val inviteGroupId = uri.getQueryParameter("groupId") ?: return
                intent.data = null

                if (!viewModel.isAlreadyJoined(inviteGroupId)) {
                    showJoinGroupDialog(inviteGroupId)
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.main_already_joined),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // 새로운 지출 등록 링크를 통해 들어온 경우
            "newExpense" -> {
                val groupId = uri.getQueryParameter("groupId") ?: return
                val expenseId = uri.getQueryParameter("expenseId") ?: return
                intent.data = null

                expenseListNavigator.navigateToExpenseListWithNewExpense(this, groupId, expenseId)
                    .also {
                        startActivity(it)
                    }
            }

            else -> {
                // Do nothing
            }
        }
    }

    private fun setSwipeRefresh() {
        binding.groupListSwiperefreshlayout.setOnRefreshListener {
            viewModel.loadGroupList()
            binding.groupListSwiperefreshlayout.isRefreshing = false
        }
    }
}
