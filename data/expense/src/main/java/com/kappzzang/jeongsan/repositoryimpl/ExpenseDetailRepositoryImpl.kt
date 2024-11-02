package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.ExpenseDetailRemoteDatasource
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailEntity
import com.kappzzang.jeongsan.mapper.ExpenseEntityMapper
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItemWithDetails
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import com.kappzzang.jeongsan.repository.ServerAuthenticationRepository
import javax.inject.Inject

class ExpenseDetailRepositoryImpl @Inject constructor(
    private val expenseDetailRemoteDatasource: ExpenseDetailRemoteDatasource,
    private val auth: ServerAuthenticationRepository
) : ExpenseDetailRepository {
    private fun getJwt(): String = auth.getSavedJwt()

    override suspend fun getExpenseDetail(expenseId: String): ExpenseItemWithDetails {
        val jwt = getJwt()

        val response = expenseDetailRemoteDatasource.getExpenseDetail(
            expenseId = expenseId,
            jwt = jwt
        )

        when (response.code()) {
            201 -> response.body()?.let {
                return mapResponseToExpenseDetail(
                    it,
                    expenseId
                )
            } ?: throw IllegalStateException("알 수 없는 오류 발생: ${response.message()}")
            404 -> throw IllegalStateException("존재하지 않는 지출")
            500 -> throw IllegalStateException(response.message())
            else -> {
                if (response.code() / 100 == 2) {
                    response.body()?.let {
                        return mapResponseToExpenseDetail(
                            it,
                            expenseId
                        )
                    } ?: throw IllegalStateException("알 수 없는 오류 발생: ${response.message()}")
                } else {
                    throw IllegalStateException("알 수 없는 오류 발생: ${response.message()}")
                }
            }
        }
    }

    private fun mapResponseToExpenseDetail(entity: ExpenseDetailEntity, expenseId: String) =
        ExpenseEntityMapper.mapDetailedExpenseEntityToModel(
            entity,
            expenseId,
            ExpenseState.NOT_CONFIRMED
        )

    override suspend fun saveExpenseDetail(edited: List<ExpenseDetailItem>, expenseId: String) {
        TODO("Not yet implemented")
    }
}
