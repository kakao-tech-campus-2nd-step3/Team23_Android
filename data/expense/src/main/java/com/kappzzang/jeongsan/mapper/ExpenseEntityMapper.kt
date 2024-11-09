package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.CategoryEntity
import com.kappzzang.jeongsan.entity.ExpenseItemEntity
import com.kappzzang.jeongsan.entity.ResponseWithExpenseIdDTO
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailEntity
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailItemEntity
import com.kappzzang.jeongsan.entity.expenselist.ExpenseRemoteEntity
import com.kappzzang.jeongsan.entity.expenselist.ExpenseRoomEntity
import com.kappzzang.jeongsan.model.ExpenseCategory
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.ExpenseItemWithCategory
import com.kappzzang.jeongsan.model.ExpenseItemWithDetails
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.model.ReceiptDetailItem
import com.kappzzang.jeongsan.util.DateConverter
import java.util.regex.Pattern

object ExpenseEntityMapper {
    private val HEXADECIMAL_PATTERN: Pattern = Pattern.compile("\\p{XDigit}+")

    fun mapExpenseEntityToModel(entity: ExpenseRoomEntity): ExpenseItemWithCategory =
        ExpenseItemWithCategory(
            item = ExpenseItem(
                id = entity.id.toString(),
                name = entity.name,
                price = entity.totalPrice,
                state = ExpenseState.entries[entity.expenseState]
            ),
            date = DateConverter.parseFromString(entity.createdTime),
            categoryColor = entity.categoryColor,
            payerServiceId = ""
        )

    fun mapExpenseEntityToModel(
        entity: ExpenseRemoteEntity,
        checked: Boolean = false
    ): ExpenseItemWithCategory = ExpenseItemWithCategory(
        item = ExpenseItem(
            id = entity.id.toString(),
            name = entity.title,
            price = entity.totalPrice,
            state = mapExpenseStateToDomainState(entity.state, checked)
        ),
        date = DateConverter.parseFromString(entity.createdAt),
        categoryColor = parseColor(entity.category.color),
        payerServiceId = entity.payerUuid ?: ""
    )

    fun mapDetailedExpenseEntityToModel(
        entity: ExpenseDetailEntity,
        expenseId: String,
        state: ExpenseState
    ): ExpenseItemWithDetails = ExpenseItemWithDetails(
        item = ExpenseItem(
            id = expenseId,
            name = entity.title,
            price = getSumOfAllDetailItems(entity.detailItems),
            state = state
        ),
        expenseImageUrl = entity.imageUrl,
        expenseDetails = entity.detailItems.map {
            mapExpenseDetailItemEntityToModel(it)
        }
    )

    private fun mapExpenseDetailItemEntityToModel(
        entity: ExpenseDetailItemEntity
    ): ExpenseDetailItem = ExpenseDetailItem(
        selectedQuantity = entity.quantityConsumed,
        itemQuantity = entity.quantity,
        id = entity.id.toString(),
        itemPrice = entity.unitPrice,
        itemName = entity.name
    )

    fun mapReceiptDetailItemToExpenseItemEntity(model: ReceiptDetailItem): ExpenseItemEntity =
        ExpenseItemEntity(
            name = model.itemName,
            quantity = model.itemQuantity,
            unitPrice = model.itemPrice
        )

    private fun isHexadecimal(input: String): Boolean {
        val matcher = HEXADECIMAL_PATTERN.matcher(input)
        return matcher.matches()
    }

    private fun parseColor(color: String): String = if (color.startsWith('#')) {
        color
    } else if (isHexadecimal(color) && (color.length == 6 || color.length == 8)) {
        "#$color"
    } else {
        throw IllegalArgumentException("The Input is not a hexcolor")
    }

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

    fun mapResponseWithExpenseEntityToModel(entity: ResponseWithExpenseIdDTO): String =
        entity.expenseId

    fun mapCategoryToModel(entity: CategoryEntity): ExpenseCategory = ExpenseCategory(
        id = entity.id.toString(),
        color = parseColor(entity.color),
        name = entity.name
    )
}
