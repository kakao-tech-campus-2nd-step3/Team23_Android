package com.kappzzang.jeongsan.ui.addexpense

import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kappzzang.jeongsan.databinding.ActivityAddExpenseBinding
import kotlinx.coroutines.flow.StateFlow

@BindingAdapter("app:items")
fun attachList(recyclerView: RecyclerView, items: StateFlow<List<ExpenseItemInput>>?) {
    items?.let {
        (recyclerView.adapter as? ExpenseItemListAdapter)?.submitList(it.value)
    }
}

object BindingAdapter {
    @BindingAdapter("stringText")
    @JvmStatic
    fun setTextValue(view: EditText, value: String?) {
        view.setText(value?:"")
    }

    @InverseBindingAdapter(attribute = "stringText")
    @JvmStatic
    fun getTextValue(view: EditText): String? {
        val parsedString = view.text.toString()
        if (parsedString.isBlank()) {
            return null
        }
        return parsedString
    }

    @BindingAdapter("integerText")
    @JvmStatic
    fun setIntegerValue(view: EditText, value: Int?) {
        view.setText(value?.toString()?:"")
    }

    @InverseBindingAdapter(attribute = "integerText")
    @JvmStatic
    fun getIntegerValue(view: EditText): Int? {
        return view.text.toString().toIntOrNull()
    }

    @BindingAdapter("integerTextAttrChanged")
    @JvmStatic
    fun setIntegerTextListener(view: EditText, listener: InverseBindingListener) {
        view.addTextChangedListener {
            listener.onChange()
        }
    }

    @BindingAdapter("stringTextAttrChanged")
    @JvmStatic
    fun setStringTextListener(view: EditText, listener: InverseBindingListener) {
        view.addTextChangedListener {
            listener.onChange()
        }
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
    }

    private fun checkIfReceiptMode(): Boolean {
        val expenseMode = intent.extras?.getString(INTENT_EXPENSE_MODE)

        return expenseMode == EXPENSE_MODE_RECEIPT
    }

    private fun initiateViewModel() {
        if (checkIfReceiptMode()) {
            viewModel.setManualMode(AddExpenseViewModel.Companion.ManualMode.RECEIPT)
            viewModel.initiateDemoData()
        }
        else {
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