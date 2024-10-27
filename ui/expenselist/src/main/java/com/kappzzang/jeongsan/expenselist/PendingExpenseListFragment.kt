package com.kappzzang.jeongsan.expenselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.expenselist.databinding.FragmentPendingExpenseListBinding
import com.kappzzang.jeongsan.expenselist.viewmodel.CompleteExpenseListPageViewModel
import com.kappzzang.jeongsan.expenselist.viewmodel.ExpenseListViewModel
import com.kappzzang.jeongsan.expenselist.viewmodel.PendingExpenseListPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PendingExpenseListFragment : Fragment() {
    private val activityViewModel: ExpenseListViewModel by activityViewModels()
    private val viewModel: PendingExpenseListPageViewModel by viewModels()
    private lateinit var binding: FragmentPendingExpenseListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPendingExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = activity
        binding.pendingExpenseListRecyclerview.adapter = ExpenseListAdapter {
            activityViewModel.clickExpenseItem(it)
        }
        binding.pendingExpenseListRecyclerview.layoutManager = LinearLayoutManager(this.context)

        viewModel.onFragmentStart(activityViewModel.groupId.value)
    }
}
