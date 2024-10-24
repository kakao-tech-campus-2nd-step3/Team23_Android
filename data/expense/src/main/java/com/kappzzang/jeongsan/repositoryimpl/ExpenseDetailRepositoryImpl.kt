package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import javax.inject.Inject

class ExpenseDetailRepositoryImpl @Inject constructor() : ExpenseDetailRepository {
    override suspend fun getExpenseDetail(): List<ExpenseDetailItem> = listOf(
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
        ExpenseDetailItem(
            "id3",
            "밍밍한 국",
            500,
            6,
            0
        ),
        ExpenseDetailItem(
            "id4",
            "상차림비",
            100,
            10,
            0
        )
    )

    override suspend fun saveExpenseDetail(edited: List<ExpenseDetailItem>) {
        // 저장하기
    }
}
