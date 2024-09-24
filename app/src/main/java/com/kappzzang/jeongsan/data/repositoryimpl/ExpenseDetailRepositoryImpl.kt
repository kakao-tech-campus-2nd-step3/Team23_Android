package com.kappzzang.jeongsan.data.repositoryimpl

import com.kappzzang.jeongsan.domain.model.ExpenseDetailItem
import com.kappzzang.jeongsan.domain.repository.ExpenseDetailRepository
import javax.inject.Inject

class ExpenseDetailRepositoryImpl @Inject constructor() :
    ExpenseDetailRepository {
        fun getExpenseDetail(): List<ExpenseDetailItem> =
            listOf(
                ExpenseDetailItem(
                    "id1",
                    "아주 맛있는 과자",
                    4000,
                    3,
                    0
                ),
                ExpenseDetailItem(
                    "id2",
                    "아주 맛없는 고기",
                    50000,
                    1,
                    0
                ),
            )
}