package com.kappzzang.jeongsan.ui.expenselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.databinding.FragmentExpenseListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpenseListFragment : Fragment() {
    private val viewModel: ExpenseListViewModel by activityViewModels()
    private lateinit var binding: FragmentExpenseListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = activity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // UI 확인을 위한 임시 코드
        binding.expenseListRecyclerview.adapter = ExpenseListAdapter {
            viewModel.selectExpense(it)
        }
        binding.expenseListRecyclerview.layoutManager = LinearLayoutManager(this.context)
        viewModel.clickOnlyNotConfirmedExpensesChipButton()
    }
}
