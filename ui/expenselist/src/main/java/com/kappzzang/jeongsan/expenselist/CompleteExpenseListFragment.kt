package com.kappzzang.jeongsan.expenselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.expenselist.databinding.FragmentCompleteExpenseListBinding
import com.kappzzang.jeongsan.expenselist.viewmodel.CompleteExpenseListPageViewModel
import com.kappzzang.jeongsan.expenselist.viewmodel.ExpenseListRefreshingState
import com.kappzzang.jeongsan.expenselist.viewmodel.ExpenseListViewModel
import com.kappzzang.jeongsan.model.ExpenseState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompleteExpenseListFragment : Fragment() {
    private val activityViewModel: ExpenseListViewModel by activityViewModels()
    private val viewModel: CompleteExpenseListPageViewModel by viewModels()
    private lateinit var binding: FragmentCompleteExpenseListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompleteExpenseListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = activity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // UI 확인을 위한 임시 코드
        binding.completeExpenseListRecyclerview.adapter = ExpenseListAdapter {
            activityViewModel.clickExpenseItem(it, ExpenseState.TRANSFERED)
        }
        binding.completeExpenseListRecyclerview.layoutManager = LinearLayoutManager(this.context)

        setSwipeRefresh()
        viewModel.onFragmentStart(activityViewModel.groupId.value)
    }

    private fun setSwipeRefresh() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.refreshState.collect {
                    if (it == ExpenseListRefreshingState.FINISHED) {
                        binding.expenseListSwipeRefreshLayout.isRefreshing = false
                        viewModel.resetRefreshState()
                    }
                }
            }
        }

        binding.expenseListSwipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }
}
