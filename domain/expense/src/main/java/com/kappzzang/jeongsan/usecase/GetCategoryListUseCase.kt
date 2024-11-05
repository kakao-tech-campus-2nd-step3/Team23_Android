package com.kappzzang.jeongsan.usecase

import com.kappzzang.jeongsan.model.ExpenseCategory
import com.kappzzang.jeongsan.repository.ExpenseRepository
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(): Result<List<ExpenseCategory>> = repository.getExpenseCategoryList()
}