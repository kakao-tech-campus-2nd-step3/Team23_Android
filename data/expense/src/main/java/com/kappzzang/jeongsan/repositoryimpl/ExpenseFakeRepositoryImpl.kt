package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.ExpenseListFakeDatasource
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.repository.ExpenseRepository
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ExpenseListCachingKey(val expenseState: ExpenseState, val groupId: String)

class ExpenseFakeRepositoryImpl @Inject constructor(
    private val dataSource: ExpenseListFakeDatasource
) : ExpenseRepository {

    private val cachedData = HashMap<ExpenseListCachingKey, ExpenseListResponse>()

    override fun getExpenseList(
        groupId: String,
        expenseState: ExpenseState
    ): Flow<ExpenseListResponse> = flow {
        emit(
            cachedData.getOrDefault(
                ExpenseListCachingKey(expenseState, groupId),
                ExpenseListResponse.emptyList()
            )
        )
        dataSource.getExpenseData(expenseState).collect {
            cachedData[ExpenseListCachingKey(expenseState, groupId)] = it
        }
        emit(
            cachedData.getOrDefault(
                ExpenseListCachingKey(expenseState, groupId),
                ExpenseListResponse.emptyList()
            )
        )
    }

    override suspend fun getExpense(id: Long) = ExpenseItem(
        id = "id",
        name = "지출 이름입니당",
        payerName = "돈 많은 사람",
        payerMemberId = "id",
        price = 15800,
        // 임시 지출 이미지 주소 (카카오테크 캠퍼스)
        expenseImageUrl = "https://www.kakaotechcampus.com/fileUpDownload/" +
            "download.do?p_savefile=gatepage_20230330053504999_1.png&p_realfile=" +
            "GNB+%EB%A1%9C%EA%B3%A0%28%EB%B3%B4%EB%9D%BC%29.png",
        date = Date(),
        state = ExpenseState.NOT_CONFIRMED,
        categoryColor = "#FFFFFF"
    )
}
