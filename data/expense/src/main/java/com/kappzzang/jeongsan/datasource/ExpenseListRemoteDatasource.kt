package com.kappzzang.jeongsan.datasource

import com.kappzzang.jeongsan.api.ReceiptRetrofitService
import com.kappzzang.jeongsan.entity.ImageEntity
import com.kappzzang.jeongsan.entity.ResponseWithExpenseIdDTO
import com.kappzzang.jeongsan.entity.SaveExpensePayloadDTO
import com.kappzzang.jeongsan.entity.expenselist.ExpenseListResponseDTO
import com.kappzzang.jeongsan.entity.expenselist.ExpenseRoomEntity
import com.kappzzang.jeongsan.mapper.ExpenseEntityMapper
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.model.ReceiptItem
import com.kappzzang.jeongsan.util.DateConverter.formatToTransferString
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

    suspend fun getExpenseList(expenseState: ExpenseState, jwt: String, groupId: String): Response<ExpenseListResponseDTO> {
        val needAdditionalQuery = checkNeedAdditionalQuery(expenseState)
        val result: Response<ExpenseListResponseDTO> = if(needAdditionalQuery) {
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

        return result
    }

    suspend fun addExpense(receiptItem: ReceiptItem, jwt: String, groupId: String): Response<ResponseWithExpenseIdDTO> {
        val postBody = SaveExpensePayloadDTO(
            title = receiptItem.title,
            items = receiptItem.expenseDetailItemList.map { ExpenseEntityMapper.mapReceiptDetailItemToExpenseItemEntity(it) },
            paymentTime = receiptItem.paymentTime.formatToTransferString(),
            image = ImageEntity(
                name = "",
                data = receiptItem.imageBase64?:"",
                url = "",
                format = IMAGE_FORMAT
            ),
            categoryId = CATEGORY_ID
        )

        val result = receiptRetrofitService.saveExpense(
            groupId = groupId,
            jwt = jwt,
            body = postBody
        )

        return result
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

    companion object {
        const val IMAGE_FORMAT = "JPG"
        const val CATEGORY_ID = 0L
    }
}
