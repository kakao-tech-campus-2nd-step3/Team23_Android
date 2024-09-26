package com.kappzzang.jeongsan.ui.expenselist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kappzzang.jeongsan.R
import com.kappzzang.jeongsan.databinding.ActivityExpenseListBinding
import com.kappzzang.jeongsan.ui.addexpense.AddExpenseActivity
import com.kappzzang.jeongsan.ui.inviteinfo.InviteInfoActivity
import com.kappzzang.jeongsan.ui.sendmessage.SendMessageActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExpenseListActivity : AppCompatActivity() {
    private val viewModel: ExpenseListViewModel by viewModels()
    private lateinit var binding: ActivityExpenseListBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.updateGroupId(intent.extras?.getString("groupId").toString())

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedExpense.collect {
                    if (it.isNotEmpty()) {
                        startExpenseDetailActivity(it)
                    }
                }
            }
        }

        val navHostFragment = supportFragmentManager.findFragmentById(
            binding.expenseListFragmentcontainerview.id
        ) as NavHostFragment
        navController = navHostFragment.navController

        setOnUpperMenuClickedListener()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.bottomnavigationview.setupWithNavController(navController)

        setOnAddExpenseFabClickedListener()

        // TODO: 임시 연결용 코드
        binding.requestExpenseFab.setOnClickListener {
            startActivity(Intent(this, SendMessageActivity::class.java))
        }
    }

    private fun setOnAddExpenseFabClickedListener() {
        fun makeAddExpenseActivityIntent(isManual: Boolean): Intent {
            val intent = Intent(
                this, AddExpenseActivity::class.java
            )

            intent.putExtra(
                AddExpenseActivity.INTENT_EXPENSE_MODE,
                if (isManual) AddExpenseActivity.EXPENSE_MODE_MANUAL else AddExpenseActivity.EXPENSE_MODE_RECEIPT
            )

            return intent
        }

        val popupMenu = PopupMenu(this, binding.addExpenseFab)
        popupMenu.menuInflater.inflate(R.menu.menu_add_expense, popupMenu.menu)
        popupMenu.setForceShowIcon(true)

        popupMenu.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when (it.itemId) {
                R.id.menu_from_camera -> {
                    startActivity(makeAddExpenseActivityIntent(false))
                    true
                }

                R.id.menu_manually -> {
                    startActivity(makeAddExpenseActivityIntent(true))
                    true
                }

                else -> false
            }
        }

        binding.addExpenseFab.setOnClickListener {
            popupMenu.show()
        }

    }

    private fun setOnUpperMenuClickedListener() {
        binding.dropdownButtonImageview.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.menu_group_setting, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                return@setOnMenuItemClickListener when (menuItem.itemId) {
                    R.id.menu_invite_status -> {
                        startActivity(Intent(this, InviteInfoActivity::class.java))
                        true
                    }

                    R.id.menu_end_group -> {
                        finish()
                        true
                    }

                    else -> {
                        false
                    }
                }
            }

            popupMenu.show()
        }
    }

    // TODO: 선택한 지출 확인용 임시 코드
    private fun startExpenseDetailActivity(expenseId: String) {
        Toast.makeText(this, expenseId, Toast.LENGTH_SHORT).show()
    }
}
