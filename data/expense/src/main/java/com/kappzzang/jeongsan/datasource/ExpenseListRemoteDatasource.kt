package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.api.ReceiptRetrofitService
import com.kappzzang.jeongsan.entity.expenselist.ExpenseListResponseDTO
import com.kappzzang.jeongsan.entity.expenselist.ExpenseRoomEntity
import com.kappzzang.jeongsan.model.ExpenseListResponse
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.model.ReceiptItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.sql.Timestamp
import javax.inject.Inject

class ExpenseListRemoteDatasource @Inject constructor(
    private val receiptRetrofitService: ReceiptRetrofitService
) {

    private fun mapExpenseStateToDtoState(state: ExpenseState): String =
        when(state){
            ExpenseState.CONFIRMED -> "ongoing"
            ExpenseState.NOT_CONFIRMED -> "ongoing"
            ExpenseState.TRANSFER_PENDING -> "pending"
            ExpenseState.TRANSFERED -> "completed"
        }

    private fun checkNeedAdditionalQuery(state: ExpenseState): Boolean =
        (state == ExpenseState.CONFIRMED || state == ExpenseState.NOT_CONFIRMED)

    private fun checkIsChecked(state: ExpenseState): Boolean =
        state == ExpenseState.CONFIRMED

    fun getExpenseList(expenseState: ExpenseState, jwt: String, groupId: String): Flow<ExpenseListResponse> = flow {
        val result: Response<ExpenseListResponseDTO> = if(checkNeedAdditionalQuery(expenseState)) {
            receiptRetrofitService.getExpenseList(
                groupId = groupId,
                jwt = jwt,
                state = mapExpenseStateToDtoState(expenseState),
                checked = checkIsChecked(expenseState)
            )
        } else{
            receiptRetrofitService.getExpenseList(
                groupId = groupId,
                jwt = jwt,
                state = mapExpenseStateToDtoState(expenseState)
            )
        }

        //emit(fakeResponse)
    }

    fun addExpense(receiptItem: ReceiptItem): String {
        val expenseEntity = ExpenseRoomEntity(
            name = receiptItem.title,
            totalPrice = receiptItem.expenseDetailItemList.sumOf { it.itemPrice * it.itemQuantity },
            createdTime = Timestamp(System.currentTimeMillis()).toString(),
            categoryColor = receiptItem.categoryColor,
            expenseState = ExpenseState.CONFIRMED.ordinal
        )

        //expenseDatabase.expenseDao().addExpense(expenseEntity)
        return expenseEntity.id.toString()
    }
}
