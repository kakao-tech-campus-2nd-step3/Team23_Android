package com.kappzzang.jeongsan.ui.expensedetail

import android.os.Bundle
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.databinding.ActivityExpenseDetailBinding
import com.kappzzang.jeongsan.domain.model.ExpenseDetailItem
import kotlinx.coroutines.flow.StateFlow

@BindingAdapter("app:detail_items")
fun attachList(
    recyclerView: RecyclerView,
    items: StateFlow<List<ExpenseDetailItem>>?,
) {
    items?.let {
        (recyclerView.adapter as? ExpenseDetailItemListAdapter)?.submitList(it.value)
    }
}

@BindingAdapter("app:detail_selection")
fun attachList(
    view: AutoCompleteTextView,
    position: Int,
) {
    if (position < 0 || position >= view.adapter.count) {
        view.setText("0", false)
        return
    }
    view.setText(view.adapter.getItem(position).toString(), false)
}

class ExpenseDetailActivity : AppCompatActivity() {
    private val binding: ActivityExpenseDetailBinding by lazy {
        ActivityExpenseDetailBinding.inflate(
            layoutInflater,
        )
    }
    private val viewModel: ExpenseDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initiateRecyclerView()
        setContentView(binding.root)
    }

    private fun initiateRecyclerView() {
        binding.expenseDetailItemListRecyclerview.adapter =
            ExpenseDetailItemListAdapter(
                this,
                object : ExpenseDetailCallback {
                    override fun onCheckedChange(
                        enable: Boolean,
                        index: Int,
                    ) {
                        viewModel.updateItemCheck(enable, index)
                    }

                    override fun onSelectedQuantityChanged(
                        quantity: Int,
                        index: Int,
                    ) {
                        viewModel.updateSelectedQuantity(quantity, index)
                    }
                },
            )
        binding.expenseDetailItemListRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}
