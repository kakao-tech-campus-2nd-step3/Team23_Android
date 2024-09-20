package com.kappzzang.jeongsan.ui.expenselist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kappzzang.jeongsan.databinding.ActivityExpenseListBinding

class ExpenseListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpenseListBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // UI 확인을 위한 임시 코드
        binding.groupNameTextview.text = "캡짱모임"
        binding.totalExpenseTextview.text = "1,000,000원"

        val navHostFragment = supportFragmentManager.findFragmentById(
            binding.expenseListFragmentcontainerview.id
        ) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomnavigationview.setupWithNavController(navController)
    }
}
