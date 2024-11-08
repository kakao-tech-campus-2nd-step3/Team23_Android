package com.kappzzang.jeongsan.expensedetail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.expensedetail.databinding.ActivityExpenseDetailBinding
import com.kappzzang.jeongsan.expensedetail.expensedetailpage.ExpenseDetailCallback
import com.kappzzang.jeongsan.expensedetail.expensedetailpage.ExpenseDetailItemListAdapter
import com.kappzzang.jeongsan.expensedetail.expensedetailpage.ExpenseDetailSaveState
import com.kappzzang.jeongsan.intentcontract.ExpenseDetailContract
import com.kappzzang.jeongsan.util.IntentHelper.getParcelableData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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
            if(viewModel.currentPage.value == ExpenseDetailPage.EXPENSE_DETAIL) {
                clickSubmitButton()
            }
            else{
                clickSwitchToPendingButton()
            }
        }

        binding.expenseDetailSecondaryButton.setOnClickListener {
            if(viewModel.currentPage.value == ExpenseDetailPage.EXPENSE_DETAIL) {
                clickToStatusButton()
            }
            else{
                clickToDetailButton()
            }
        }
    }

    private fun clickSubmitButton(){
        viewModel.clickSaveDetailsAndClose()
    }

    private fun clickToStatusButton() {
        viewModel.clickToSelectionStatus()
    }

    private fun clickToDetailButton() {
        viewModel.clickToExpenseDetail()
    }

    private fun clickSwitchToPendingButton(){
        viewModel.clickSwitchToPending()
    }

    private fun collectStateFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentPage.collect {
                    try {
                        when (it) {
                            ExpenseDetailPage.EXPENSE_DETAIL -> navController.navigate(R.id.action_selectionStatusFragment_to_expenseDetailFragment)
                            ExpenseDetailPage.SELECTION_STATUS -> navController.navigate(R.id.action_expenseDetailFragment_to_selectionStatusFragment)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.expenseDetailSaveState.collect {
                    if(it == ExpenseDetailSaveState.SUCCESS){
                        finish()
                    }
                    else if(it == ExpenseDetailSaveState.FAILED){
                        Toast.makeText(this@ExpenseDetailActivity, R.string.expense_detail_error_message_save_expense_info, Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun getIntentData() {
        val expenseId = intent?.getParcelableData<String>(ExpenseDetailContract.EXPENSE_ID)
        val groupId = intent?.getParcelableData<String>(ExpenseDetailContract.GROUP_ID)
        val editable = intent?.getParcelableData<Boolean>(ExpenseDetailContract.EDITABLE)

        if (expenseId == null || groupId == null || editable == null) {
            throwExpenseDataLoadFailError()
            return
        }

        viewModel.setInitialData(expenseId, groupId, editable, true)
    }

    private fun throwExpenseDataLoadFailError() {
        Toast.makeText(
            this,
            getString(R.string.expense_detail_error_message_load_expense_info),
            Toast.LENGTH_LONG
        ).show()
        finish()
    }
}
