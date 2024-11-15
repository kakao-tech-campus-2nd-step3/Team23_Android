package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.CategoryEntity
import com.kappzzang.jeongsan.entity.ResponseWithExpenseIdDTO
import com.kappzzang.jeongsan.entity.TransferItemEntity
import com.kappzzang.jeongsan.model.ExpenseCategory
import com.kappzzang.jeongsan.model.TransferDetailItem
import com.kappzzang.jeongsan.util.ColorParser

object ExpenseEntityMapper {
    fun mapResponseWithExpenseEntityToModel(entity: ResponseWithExpenseIdDTO): String =
        entity.expenseId.toString()

    fun mapCategoryToModel(entity: CategoryEntity): ExpenseCategory = ExpenseCategory(
        id = entity.id.toString(),
        color = ColorParser.parseColor(entity.color),
        name = entity.name
    )

    fun mapTransferEntityToModel(entity: TransferItemEntity): TransferDetailItem =
        TransferDetailItem(
            name = entity.name,
            serviceId = entity.serviceId.toString(),
            profileImageUrl = entity.profileImageUrl,
            fee = entity.expenseToTransfer,
            memberId = entity.memberId.toString()
        )
}
