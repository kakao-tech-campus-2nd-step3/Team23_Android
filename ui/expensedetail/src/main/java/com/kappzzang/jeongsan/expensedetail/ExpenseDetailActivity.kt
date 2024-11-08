package com.kappzzang.jeongsan.expensedetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.expensedetail.databinding.ActivityExpenseDetailBinding
import com.kappzzang.jeongsan.expensedetail.expensedetailpage.ExpenseDetailCallback
import com.kappzzang.jeongsan.expensedetail.expensedetailpage.ExpenseDetailItemListAdapter
import com.kappzzang.jeongsan.intentcontract.ExpenseDetailContract
import com.kappzzang.jeongsan.util.IntentHelper.getParcelableData
import dagger.hilt.android.AndroidEntryPoint

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

        navController.navigate(R.id.selectionStatusFragment)

        // TODO: 임시 연결용 코드
        binding.expenseDetailSubmitButton.setOnClickListener {
            //viewModel.saveExpenseDetail()
            finish()
        }
    }

    private fun initiateViewModel() {
        getIntentData()
    }

    private fun getIntentData() {
        val expenseId = intent?.getParcelableData<String>(ExpenseDetailContract.EXPENSE_ID)
        val groupId = intent?.getParcelableData<String>(ExpenseDetailContract.GROUP_ID)
        val editable = intent?.getParcelableData<Boolean>(ExpenseDetailContract.EDITABLE)

        if (expenseId == null || groupId == null || editable == null) {
            throwExpenseDataLoadFailError()
            return
        }

        viewModel.setInitialData(expenseId, groupId, editable)
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
