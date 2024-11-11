package com.kappzzang.jeongsan.expensedetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.kappzzang.jeongsan.build_config.BuildConfig
import com.kappzzang.jeongsan.expensedetail.databinding.ActivityExpenseDetailBinding
import com.kappzzang.jeongsan.intentcontract.ExpenseDetailContract
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.util.IntentHelper.getParcelableData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExpenseDetailActivity : AppCompatActivity() {
    private val binding: ActivityExpenseDetailBinding by lazy {
        val mBinding = ActivityExpenseDetailBinding.inflate(
            layoutInflater
        )

        mBinding.viewModel = viewModel
        mBinding.lifecycleOwner = this
        mBinding
    }
    private val viewModel: ExpenseDetailViewModel by viewModels()
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(
            binding.expenseDetailFragmentContainerView.id
        ) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            initiateViewModel()
        }

        setContentView(binding.root)

        setButtonsOnClickListener()
        collectStateFlow()
    }

    private fun initiateViewModel() {
        getIntentData()
    }

    private fun setButtonsOnClickListener() {
        binding.expenseDetailPrimaryButton.setOnClickListener {
            viewModel.clickLeftButton()
        }

        binding.expenseDetailSecondaryButton.setOnClickListener {
            viewModel.clickRightButton()
        }
    }

    private fun collectStateFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentPage.collect {
                    try {
                        when (it) {
                            ExpenseDetailPage.EXPENSE_DETAIL -> navController.navigate(
                                R.id.action_selectionStatusFragment_to_expenseDetailFragment
                            )

                            ExpenseDetailPage.SELECTION_STATUS -> navController.navigate(
                                R.id.action_expenseDetailFragment_to_selectionStatusFragment
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.expenseDetailState.collect {
                    if (it == ExpenseDetailState.SUCCESS) {
                        finish()
                    } else if (it == ExpenseDetailState.FAILED) {
                        Toast.makeText(
                            this@ExpenseDetailActivity,
                            R.string.expense_detail_error_message_save_expense_info,
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun getIntentData() {
        val expenseId = intent?.getParcelableData<String>(ExpenseDetailContract.EXPENSE_ID)
        val groupId = intent?.getParcelableData<String>(ExpenseDetailContract.GROUP_ID)
        val expenseState = intent?.getParcelableData<ExpenseState>(
            ExpenseDetailContract.EXPENSE_STATE
        )
        val isPayer = intent?.getParcelableData<Boolean>(ExpenseDetailContract.IS_PAYER)

        if (expenseId == null || groupId == null || expenseState == null || isPayer == null) {
            throwExpenseDataLoadFailError()
            return
        }

        viewModel.setInitialData(
            expenseId,
            groupId,
            expenseState,
            isPayer || (BuildConfig.DEBUG && ALWAYS_PAYER_FLAG)
        )
    }

    private fun throwExpenseDataLoadFailError() {
        Toast.makeText(
            this,
            getString(R.string.expense_detail_error_message_load_expense_info),
            Toast.LENGTH_LONG
        ).show()
        finish()
    }

    companion object {
        // 항상 결제자로 간주하게 설정하는 플래그; API가 완성되면 false로 수정
        private const val ALWAYS_PAYER_FLAG = false
    }
}
