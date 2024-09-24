package com.kappzzang.jeongsan.data.repositoryimpl

import com.kappzzang.jeongsan.domain.model.ExpenseItem
import com.kappzzang.jeongsan.domain.model.ExpenseState
import com.kappzzang.jeongsan.domain.repository.ExpenseRepository
import com.kappzzang.jeongsan.ui.Member
import java.util.Date
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor() : ExpenseRepository {
    override suspend fun getExpense(id: Long) = ExpenseItem(
        id = "id",
        name = "지출 이름입니당",
        payer = Member("돈 많은 사람"),
        price = 15800,
        date = Date(),
        state = ExpenseState.NOT_CONFIRMED,
        categoryColor = "#FFFFFF"
    )
}
