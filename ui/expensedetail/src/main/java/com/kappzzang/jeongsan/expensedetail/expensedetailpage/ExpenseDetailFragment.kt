package com.kappzzang.jeongsan.expensedetail.expensedetailpage

import android.os.Bundle
import android.util.Log
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
import com.kappzzang.jeongsan.expensedetail.ExpenseDetailViewModel
import com.kappzzang.jeongsan.expensedetail.databinding.FragmentExpenseDetailBinding
import dagger.hilt.android.AndroidEntryPoint
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
        if (savedInstanceState == null) {
            initiateData()
            Log.d("KSC", "ExpenseDetailFragment View Created")

        }
        initiateRecyclerView()
        collectStateFlow()
    }

    private fun collectStateFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.expenseDetailSaveState.collect {
                    processExpenseDetailSaveResult(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                activityViewModel.expenseDetailSaveState.collect {
                    if(it == ExpenseDetailSaveState.UPLOADING) {
                        viewModel.saveExpenseDetail()
                    }
                }
            }
        }
    }

    private fun processExpenseDetailSaveResult(result: ExpenseDetailSaveState) {
        if(result == ExpenseDetailSaveState.SUCCESS) {
            sendExpenseUploadResult(true)
        }
        else if(result == ExpenseDetailSaveState.FAILED) {
            sendExpenseUploadResult(false)
        }
    }

    private fun sendExpenseUploadResult(isSuccess: Boolean) {
        activityViewModel.setSaveResult(isSuccess)
    }

    private fun initiateData() {
        viewModel.setInitialData(
            expenseId = activityViewModel.expenseId,
            groupId = activityViewModel.groupId,
            editable = activityViewModel.editable
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
