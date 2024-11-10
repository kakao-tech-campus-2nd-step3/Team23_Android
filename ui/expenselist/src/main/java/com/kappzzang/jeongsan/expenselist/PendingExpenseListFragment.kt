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
import com.kappzzang.jeongsan.expenselist.databinding.FragmentPendingExpenseListBinding
import com.kappzzang.jeongsan.expenselist.viewmodel.ExpenseListRefreshingState
import com.kappzzang.jeongsan.expenselist.viewmodel.ExpenseListViewModel
import com.kappzzang.jeongsan.expenselist.viewmodel.PendingExpenseListPageViewModel
import com.kappzzang.jeongsan.model.ExpenseState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        binding.pendingExpenseListRecyclerview.adapter = ExpenseListAdapter { expenseId, isPayer ->
            activityViewModel.clickExpenseItem(expenseId, ExpenseState.TRANSFER_PENDING, isPayer)
        }
        binding.pendingExpenseListRecyclerview.layoutManager = LinearLayoutManager(this.context)
        viewModel.onFragmentStart(activityViewModel.groupId.value)

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

    override fun onResume() {
        viewModel.onFragmentReload()
        super.onResume()
    }
}
