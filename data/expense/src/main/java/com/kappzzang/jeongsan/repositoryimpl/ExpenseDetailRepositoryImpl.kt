package com.kappzzang.jeongsan.repositoryimpl

import com.kappzzang.jeongsan.datasource.ExpenseDetailRemoteDatasource
import com.kappzzang.jeongsan.datasource.ExpenseListRemoteDatasource
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailEntity
import com.kappzzang.jeongsan.mapper.ExpenseDetailMapper
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItemWithDetails
import com.kappzzang.jeongsan.model.ExpenseSelectionStatus
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.repository.ExpenseDetailRepository
import javax.inject.Inject

class ExpenseDetailRepositoryImpl @Inject constructor(
    private val expenseDetailRemoteDatasource: ExpenseDetailRemoteDatasource,
    private val expenseListRemoteDatasource: ExpenseListRemoteDatasource
) : ExpenseDetailRepository {

    override suspend fun getExpenseDetail(expenseId: String): Result<ExpenseItemWithDetails> =
        expenseDetailRemoteDatasource.getExpenseDetail(
            expenseId = expenseId
        ).mapCatching {
            mapResponseToExpenseDetail(it, expenseId)
        }

    private fun mapResponseToExpenseDetail(entity: ExpenseDetailEntity, expenseId: String) =
        ExpenseDetailMapper.mapDetailedExpenseEntityToModel(
            entity,
            expenseId,
            ExpenseState.NOT_CONFIRMED
        )

    override suspend fun saveExpenseDetail(
        edited: List<ExpenseDetailItem>,
        expenseId: String,
        groupId: String
    ): Result<Unit> = expenseDetailRemoteDatasource.updateExpenseDetail(
        expenseId = expenseId,
        groupId = groupId,
        edited = edited
    )

    override suspend fun getExpenseSelectionStatus(
        expenseId: String
    ): Result<ExpenseSelectionStatus> = expenseDetailRemoteDatasource.getExpenseSelectionStatus(
        expenseId = expenseId
    ).mapCatching {
        ExpenseDetailMapper.mapExpenseSelectionStatusEntityToModel(it)
    }

    override suspend fun updateExpenseStateToPending(
        expenseId: String,
        groupId: String
    ): Result<Unit> = expenseListRemoteDatasource.updateExpenseState(
        state = ExpenseState.TRANSFER_PENDING,
        expenseItemIdList = listOf(expenseId),
        groupId = groupId
    )

    override suspend fun updateExpenseStateToOngoing(
        expenseId: String,
        groupId: String
    ): Result<Unit> = expenseListRemoteDatasource.updateExpenseState(
        state = ExpenseState.CONFIRMED,
        expenseItemIdList = listOf(expenseId),
        groupId = groupId
    )
}
