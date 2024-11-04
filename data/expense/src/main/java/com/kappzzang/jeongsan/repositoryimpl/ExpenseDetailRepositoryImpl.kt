package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.ExpenseDetailRemoteDatasource
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailEntity
import com.kappzzang.jeongsan.mapper.ExpenseEntityMapper
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItemWithDetails
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import com.kappzzang.jeongsan.repository.ServerAuthenticationRepository
import retrofit2.Response
import javax.inject.Inject

class ExpenseDetailRepositoryImpl @Inject constructor(
    private val expenseDetailRemoteDatasource: ExpenseDetailRemoteDatasource
) : ExpenseDetailRepository {

    override suspend fun getExpenseDetail(expenseId: String): Result<ExpenseItemWithDetails> =
        expenseDetailRemoteDatasource.getExpenseDetail(
            expenseId = expenseId
        ).mapCatching {
            mapResponseToExpenseDetail(it, expenseId)
        }

    private fun mapResponseToExpenseDetail(entity: ExpenseDetailEntity, expenseId: String) =
        ExpenseEntityMapper.mapDetailedExpenseEntityToModel(
            entity,
            expenseId,
            ExpenseState.NOT_CONFIRMED
        )

    override suspend fun saveExpenseDetail(edited: List<ExpenseDetailItem>, expenseId: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}
