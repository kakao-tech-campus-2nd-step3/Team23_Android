package com.kappzzang.jeongsan.ui.expenselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.databinding.FragmentCompleteExpenseListBinding
import com.kappzzang.jeongsan.domain.model.ExpenseItem

class CompleteExpenseListFragment : Fragment() {

    private lateinit var binding: FragmentCompleteExpenseListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompleteExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // UI 확인을 위한 임시 코드
        binding.completeExpenseListRecyclerview.adapter =
            ExpenseListAdapter(createDemoExpenseItemList())
        binding.completeExpenseListRecyclerview.layoutManager = LinearLayoutManager(this.context)
    }

    private fun createDemoExpenseItemList(): List<ExpenseItem> = (1..9).map { i ->
        ExpenseItem("송금 완료 내역 $i", "$i,000원", "2024.09.0$i", "#0000FF")
    }
}
