package com.kappzzang.jeongsan.expenselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.databinding.FragmentCompleteExpenseListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompleteExpenseListFragment : Fragment() {
    private val viewModel: ExpenseListViewModel by activityViewModels()
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
            viewModel.clickExpenseItem(it)
        }
        binding.completeExpenseListRecyclerview.layoutManager = LinearLayoutManager(this.context)
        viewModel.clickSentCompleteMenuButton()
    }
}
