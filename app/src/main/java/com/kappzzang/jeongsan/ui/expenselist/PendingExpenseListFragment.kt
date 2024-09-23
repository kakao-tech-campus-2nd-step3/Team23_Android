package com.kappzzang.jeongsan.ui.expenselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.databinding.FragmentPendingExpenseListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PendingExpenseListFragment : Fragment() {
    private val viewModel:ExpenseListViewModel by activityViewModels()
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
        binding.pendingExpenseListRecyclerview.adapter = ExpenseListAdapter()
        binding.pendingExpenseListRecyclerview.layoutManager = LinearLayoutManager(this.context)

        viewModel.clickPendSendingMenuButton()
    }
}
