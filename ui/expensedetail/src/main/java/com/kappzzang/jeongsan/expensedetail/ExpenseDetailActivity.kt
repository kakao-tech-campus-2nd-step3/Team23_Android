package com.kappzzang.jeongsan.expensedetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.expensedetail.databinding.ActivityExpenseDetailBinding
import com.kappzzang.jeongsan.intentcontract.ExpenseDetailContract
import com.kappzzang.jeongsan.util.IntentHelper.getParcelableData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpenseDetailActivity : AppCompatActivity() {
    private val binding: ActivityExpenseDetailBinding by lazy {
        ActivityExpenseDetailBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: ExpenseDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        if (savedInstanceState == null) {
            initiateViewModel()
        }

        initiateRecyclerView()
        setContentView(binding.root)

        // TODO: 임시 연결용 코드
        binding.expenseDetailSubmitButton.setOnClickListener {
            viewModel.saveExpenseDetail()
            finish()
        }
    }

    private fun initiateRecyclerView() {
        val expenseDetailAdapter = ExpenseDetailItemListAdapter(
            this,
            object : ExpenseDetailCallback {
                override fun onCheckedChange(enable: Boolean, index: Int) {
                    viewModel.updateItemCheck(enable, index)
                }

                override fun onSelectedQuantityChanged(quantity: Int, index: Int) {
                    viewModel.updateSelectedQuantity(quantity, index)
                }
            }
        )
        binding.expenseDetailItemListRecyclerview.adapter = expenseDetailAdapter
        binding.expenseDetailItemListRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
