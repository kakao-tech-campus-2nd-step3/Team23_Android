package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.ExpenseListRemoteDatasource
import com.kappzzang.jeongsan.entity.expenselist.ExpenseListResponseDTO
import com.kappzzang.jeongsan.mapper.ExpenseEntityMapper
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.ExpenseItemWithDetails
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.repository.ExpenseRepository
import com.kappzzang.jeongsan.repository.ServerAuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class ExpenseListCachingKey(val expenseState: ExpenseState, val groupId: String)

class ExpenseListRepositoryImpl @Inject constructor(
    private val dataSource: ExpenseListRemoteDatasource,
    private val auth: ServerAuthenticationRepository
) : ExpenseRepository {

    private val cachedData = HashMap<ExpenseListCachingKey, ExpenseListResponse>()

    private fun getJwt(): String = auth.getSavedJwt()
    private fun isJwtValid(jwt: String): Boolean = (jwt != "")

    private fun mapResponseBody(body: ExpenseListResponseDTO): ExpenseListResponse {
        val expenses = body.expenseList.map { ExpenseEntityMapper.mapExpenseEntityToModel(it) }
        return ExpenseListResponse(
            totalExpenseToSend = body.totalPrice.toInt(),
            expenseList = expenses,
            totalPrice = body.totalPrice.toInt()
        )
    }

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

        val jwt = getJwt()
        if (!isJwtValid(jwt)) {
            cachedData[ExpenseListCachingKey(expenseState, groupId)] =
                ExpenseListResponse.emptyList()
        } else {
            val response = dataSource.getExpenseList(
                expenseState,
                groupId = groupId,
                jwt = jwt
            )

            response.body()?.let {
                cachedData[ExpenseListCachingKey(expenseState, groupId)] = mapResponseBody(it)
            }
        }
        emit(
            cachedData.getOrDefault(
                ExpenseListCachingKey(expenseState, groupId),
                ExpenseListResponse.emptyList()
            )
        )
    }

    override suspend fun getExpense(id: Long) = ExpenseItemWithDetails(
        item = ExpenseItem(
            id = id.toString(),
            state = ExpenseState.NOT_CONFIRMED,
            name = "지출 이름입니당",
            price = 15800
        ),
        // 임시 지출 이미지 주소 (카카오테크 캠퍼스)
        expenseImageUrl = "https://www.kakaotechcampus.com/fileUpDownload/" +
                "download.do?p_savefile=gatepage_20230330053504999_1.png&p_realfile=" +
                "GNB+%EB%A1%9C%EA%B3%A0%28%EB%B3%B4%EB%9D%BC%29.png",
        expenseDetails = listOf(
            ExpenseDetailItem("", "1", 200, 1, 0),
            ExpenseDetailItem("", "2", 300, 4, 1)
        )
    )
}
