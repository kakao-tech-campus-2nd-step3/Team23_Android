package com.kappzzang.jeongsan.expensedetail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.expensedetail.databinding.ActivityExpenseDetailBinding
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
}
