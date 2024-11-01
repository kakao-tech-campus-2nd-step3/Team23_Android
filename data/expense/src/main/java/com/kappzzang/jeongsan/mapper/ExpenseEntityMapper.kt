package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.ExpenseDetailEntity
import com.kappzzang.jeongsan.entity.ExpenseDetailItemEntity
import com.kappzzang.jeongsan.entity.ExpenseItemEntity
import com.kappzzang.jeongsan.entity.expenselist.ExpenseRemoteEntity
import com.kappzzang.jeongsan.entity.expenselist.ExpenseRoomEntity
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.ExpenseItemWithCategory
import com.kappzzang.jeongsan.model.ExpenseItemWithDetails
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.model.ReceiptDetailItem
import com.kappzzang.jeongsan.util.DateConverter

object ExpenseEntityMapper {
    fun mapExpenseEntityToModel(entity: ExpenseRoomEntity): ExpenseItemWithCategory =
        ExpenseItemWithCategory(
            item = ExpenseItem(
                id = entity.id.toString(),
                name = entity.name,
                price = entity.totalPrice,
                state = ExpenseState.entries[entity.expenseState],
            ),
            date = DateConverter.parseFromString(entity.createdTime),
            categoryColor = entity.categoryColor
        )

    fun mapExpenseEntityToModel(
        entity: ExpenseRemoteEntity,
        checked: Boolean = false
    ): ExpenseItemWithCategory =
        ExpenseItemWithCategory(
            item = ExpenseItem(
                id = entity.id.toString(),
                name = entity.title,
                price = entity.totalPrice,
                state = mapExpenseStateToDomainState(entity.state, checked),
            ),
            date = DateConverter.parseFromString(entity.createdAt),
            categoryColor = entity.category.color,
        )

    fun mapDetailedExpenseEntityToModel(
        entity: ExpenseDetailEntity,
        expenseId: String,
        state: ExpenseState
    ): ExpenseItemWithDetails =
        ExpenseItemWithDetails(
            item = ExpenseItem(
                id = expenseId,
                name = entity.title,
                price = getSumOfAllDetailItems(entity.detailItems),
                state = state,
            ),
            expenseImageUrl = entity.imageUrl,
            expenseDetails = entity.detailItems.map {
                mapExpenseDetailEntityToModel(it)
            }
        )

    fun mapExpenseDetailEntityToModel(
        entity: ExpenseDetailItemEntity
    ): ExpenseDetailItem =
        ExpenseDetailItem(
            selectedQuantity = entity.quantityConsumed,
            itemQuantity = entity.quantity,
            id = entity.id.toString(),
            itemPrice = entity.unitPrice,
            itemName = entity.name
        )

    fun mapReceiptDetailItemToExpenseItemEntity(
        model: ReceiptDetailItem
    ): ExpenseItemEntity =
        ExpenseItemEntity(
            name = model.itemName,
            quantity = model.itemQuantity,
            unitPrice = model.itemPrice
        )

    private fun getSumOfAllDetailItems(detailItems: List<ExpenseDetailItemEntity>): Int =
        detailItems.sumOf {
            it.unitPrice * it.quantity
        }


    private fun mapExpenseStateToDomainState(state: String, checked: Boolean): ExpenseState {
        val trimmed = state.lowercase().trim()
        return when (trimmed) {
            "pending" -> ExpenseState.TRANSFER_PENDING
            "completed" -> ExpenseState.TRANSFERED
            "ongoing" -> {
                if (checked) ExpenseState.CONFIRMED else ExpenseState.NOT_CONFIRMED
            }

            else -> {
                throw IllegalStateException("Invalid Expense State")
            }
        }
    }
}
