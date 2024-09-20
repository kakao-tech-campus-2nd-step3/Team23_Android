package com.kappzzang.jeongsan.ui.expenselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kappzzang.jeongsan.databinding.FragmentPendingExpenseListBinding

class PendingExpenseListFragment : Fragment() {

    private lateinit var binding: FragmentPendingExpenseListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPendingExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // UI 확인을 위한 임시 코드
        binding.myTotalExpenseTextview.text = "100,000원"
        binding.pendingExpenseListRecyclerview.adapter =
            ExpenseListAdapter(createDemoExpenseItemList())
        binding.pendingExpenseListRecyclerview.layoutManager = LinearLayoutManager(this.context)
    }

    private fun createDemoExpenseItemList(): List<ExpenseViewItem> = (1..9).map { i ->
        ExpenseViewItem("송금 대기 내역 $i", "$i,000원", "2024.09.0$i", "#00FF00")
    }
}
