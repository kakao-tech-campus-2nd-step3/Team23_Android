package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.CategoryEntity
import com.kappzzang.jeongsan.entity.ExpenseItemEntity
import com.kappzzang.jeongsan.entity.ResponseWithExpenseIdDTO
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailEntity
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailItemEntity
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseSelectionItemEntity
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseSelectionResponseDTO
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseSelectorEntity
import com.kappzzang.jeongsan.entity.expenselist.ExpenseRemoteEntity
import com.kappzzang.jeongsan.entity.expenselist.ExpenseRoomEntity
import com.kappzzang.jeongsan.model.ExpenseCategory
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.model.ExpenseItemWithCategory
import com.kappzzang.jeongsan.model.ExpenseItemWithDetails
import com.kappzzang.jeongsan.model.ExpenseSelectionStatus
import com.kappzzang.jeongsan.model.ExpenseSelectionStatusItem
import com.kappzzang.jeongsan.model.ExpenseSelectorInfo
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.model.ReceiptDetailItem
import com.kappzzang.jeongsan.util.DateConverter

object ExpenseDetailMapper {
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

    private fun getSumOfAllDetailItems(detailItems: List<ExpenseDetailItemEntity>): Int =
        detailItems.sumOf {
            it.unitPrice * it.quantity
        }

    fun mapExpenseSelectionStatusEntityToModel(entity: ExpenseSelectionResponseDTO): ExpenseSelectionStatus =
        ExpenseSelectionStatus(
            name = entity.title,
            items = entity.items.map { mapExpenseSelectionStatusItemEntityToModel(it) }
        )

    private fun mapExpenseSelectionStatusItemEntityToModel(entityItem: ExpenseSelectionItemEntity): ExpenseSelectionStatusItem =
        ExpenseSelectionStatusItem(
            itemId = entityItem.id.toString(),
            name = entityItem.name,
            unitPrice = entityItem.unitPrice,
            quantity = entityItem.quantity,
            selectorList = entityItem.selectorList.map { mapExpenseSelectorEntityToModel(it) }
        )


    private fun mapExpenseSelectorEntityToModel(selector: ExpenseSelectorEntity): ExpenseSelectorInfo =
        ExpenseSelectorInfo(
            name = selector.name,
            selectedQuantity = selector.selectedQuantity,
            profileImageUrl = selector.profileImage
        )
}
