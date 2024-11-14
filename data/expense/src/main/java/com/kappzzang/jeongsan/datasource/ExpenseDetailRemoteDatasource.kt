package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.api.ReceiptRetrofitService
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailEntity
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailSelectionInfoEntity
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseSelectionResponseDTO
import com.kappzzang.jeongsan.entity.expensedetail.UpdateExpenseDetailPayloadDTO
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import javax.inject.Inject

class ExpenseDetailRemoteDatasource @Inject constructor(
    private val receiptRetrofitService: ReceiptRetrofitService
) {
    suspend fun getExpenseDetail(expenseId: String): Result<ExpenseDetailEntity> {
        val response = try {
            receiptRetrofitService.getExpenseDetail(
                expenseId = expenseId
            )
        } catch (e: Exception) {
            return (Result.failure(e))
        }

        return processResponseOnResponseData(response)
    }

    suspend fun getExpenseSelectionStatus(expenseId: String): Result<ExpenseSelectionResponseDTO> {
        val response = try {
            receiptRetrofitService.getExpenseSelectionStatus(
                expenseId = expenseId
            )
        } catch (e: Exception) {
            return (Result.failure(e))
        }

        return processResponseOnResponseData(response)
    }

    suspend fun updateExpenseDetail(
        expenseId: String,
        groupId: String,
        edited: List<ExpenseDetailItem>
    ): Result<Unit> {
        val response = try {
            val body = UpdateExpenseDetailPayloadDTO(
                items = edited.map {
                    ExpenseDetailSelectionInfoEntity(
                        quantity = it.selectedQuantity,
                        itemId = it.id.toInt()
                    )
                }
            )
            receiptRetrofitService.updateExpenseDetail(
                expenseId = expenseId,
                groupId = groupId,
                body = body
            )
        } catch (e: Exception) {
            return (Result.failure(e))
        }

        return processResponse(response)
    }
}
