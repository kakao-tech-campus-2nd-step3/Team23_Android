package com.kappzzang.jeongsan.expensedetail.expensedetailpage

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
import com.kappzzang.jeongsan.expensedetail.ExpenseDetailState
import com.kappzzang.jeongsan.expensedetail.ExpenseDetailViewModel
import com.kappzzang.jeongsan.expensedetail.databinding.FragmentExpenseDetailBinding
import com.kappzzang.jeongsan.expensedetail.editable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExpenseDetailFragment : Fragment() {
    private lateinit var binding: FragmentExpenseDetailBinding
    private val viewModel: ExpenseDetailFragmentViewModel by viewModels()
    private val activityViewModel: ExpenseDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpenseDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = activity
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateData()
        initiateRecyclerView()
        collectStateFlow()
    }

    private fun collectStateFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.expenseDetailSaveState.collect {
                    processExpenseDetailSaveResult(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                activityViewModel.expenseDetailState.collect {
                    if (it == ExpenseDetailState.UPLOADING) {
                        viewModel.saveExpenseDetail()
                    }
                }
            }
        }
    }

    private fun processExpenseDetailSaveResult(result: ExpenseDetailState) {
        if (result == ExpenseDetailState.SUCCESS) {
            activityViewModel.setSaveResult(true)
        } else if (result == ExpenseDetailState.FAILED) {
            activityViewModel.setSaveResult(false)
        }
    }

    private fun initiateData() {
        viewModel.setInitialData(
            expenseId = activityViewModel.expenseId,
            groupId = activityViewModel.groupId,
            editable = activityViewModel.expenseState.value.editable()
        )
    }

    private fun initiateRecyclerView() {
        val expenseDetailAdapter = ExpenseDetailItemListAdapter(
            this.requireContext(),
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
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}
