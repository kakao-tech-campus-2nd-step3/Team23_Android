package com.kappzzang.jeongsan.expensedetail.selectionstatus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.expensedetail.ExpenseDetailViewModel
import com.kappzzang.jeongsan.expensedetail.databinding.FragmentSelectionStatusBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectionStatusFragment : Fragment() {
    val viewModel: SelectionStatusViewModel by viewModels()
    private val activityViewModel: ExpenseDetailViewModel by activityViewModels()
    private lateinit var binding: FragmentSelectionStatusBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectionStatusBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = activity
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.initExpenseId(activityViewModel.expenseId)
        }
        initiateRecyclerView()
    }

    private fun initiateRecyclerView() {
        val adapter = SelectionStatusItemListAdapter(this.requireContext())
        binding.expenseSelectionItemListRecyclerview.adapter = adapter
        binding.expenseSelectionItemListRecyclerview.layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}
