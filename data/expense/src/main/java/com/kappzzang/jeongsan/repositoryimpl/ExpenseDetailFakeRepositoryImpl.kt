package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.ExpenseItemWithDetails
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import javax.inject.Inject

class ExpenseDetailFakeRepositoryImpl @Inject constructor() : ExpenseDetailRepository {
    override suspend fun getExpenseDetail(expenseId: String): Result<ExpenseItemWithDetails> {
        val details =
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
        val expenseItemWithDetails = ExpenseItemWithDetails(
            expenseImageUrl = FAKE_IMAGE_URL,
            item = ExpenseItem(
                id = "id",
                state = ExpenseState.NOT_CONFIRMED,
                name = "지출 이름입니당",
                price = 15800
            ),
            expenseDetails = details
        )
        return Result.success(expenseItemWithDetails)
    }

    override suspend fun saveExpenseDetail(
        edited: List<ExpenseDetailItem>,
        expenseId: String
    ): Result<Unit> = Result.success(Unit)

    companion object {
        const val FAKE_IMAGE_URL = "https://www.kakaotechcampus.com/fileUpDownload/" +
                "download.do?p_savefile=gatepage_20230330053504999_1.png&p_realfile=" +
                "GNB+%EB%A1%9C%EA%B3%A0%28%EB%B3%B4%EB%9D%BC%29.png"
    }
}
