package com.kappzzang.jeongsan.ui.expenselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kappzzang.jeongsan.databinding.FragmentCompleteExpenseListBinding

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
}
