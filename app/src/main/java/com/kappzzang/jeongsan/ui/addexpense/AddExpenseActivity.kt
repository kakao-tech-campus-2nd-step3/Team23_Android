package com.kappzzang.jeongsan.ui.addexpense

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.databinding.ActivityAddExpenseBinding
import com.kappzzang.jeongsan.ui.expensedetail.ExpenseDetailActivity
import kotlinx.coroutines.flow.StateFlow

@BindingAdapter("app:items")
fun attachList(recyclerView: RecyclerView, items: StateFlow<List<ExpenseItemInput>>?) {
    items?.let {
        (recyclerView.adapter as? ExpenseItemListAdapter)?.submitList(it.value)
    }
}

class AddExpenseActivity : AppCompatActivity() {
    private val viewModel: AddExpenseViewModel by viewModels()
    private val binding: ActivityAddExpenseBinding by lazy {
        ActivityAddExpenseBinding.inflate(
            layoutInflater
        )
    }

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
        binding.addexpenseSubmitButton.setOnClickListener {
            startActivity(Intent(this, ExpenseDetailActivity::class.java))
            finish()
        }
    }

    private fun checkIfReceiptMode(): Boolean {
        val expenseMode = intent.extras?.getString(INTENT_EXPENSE_MODE)

        return expenseMode == EXPENSE_MODE_RECEIPT
    }

    private fun initiateViewModel() {
        if (checkIfReceiptMode()) {
            viewModel.setManualMode(AddExpenseViewModel.Companion.ManualMode.RECEIPT)
            viewModel.initiateDemoData()
        } else {
            viewModel.setManualMode(AddExpenseViewModel.Companion.ManualMode.MANUAL)
        }
    }

    private fun initiateRecyclerView() {
        with(binding.addexpenseItemListRecyclerview) {
            adapter = ExpenseItemListAdapter()
            layoutManager =
                LinearLayoutManager(this@AddExpenseActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    companion object {
        const val INTENT_EXPENSE_MODE = "expenseMode"
        const val EXPENSE_MODE_MANUAL = "manual"
        const val EXPENSE_MODE_RECEIPT = "receipt"
    }
}
