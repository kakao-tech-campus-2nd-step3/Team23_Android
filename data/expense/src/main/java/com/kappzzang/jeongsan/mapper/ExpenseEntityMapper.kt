package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.CategoryEntity
import com.kappzzang.jeongsan.entity.ResponseWithExpenseIdDTO
import com.kappzzang.jeongsan.model.ExpenseCategory
import com.kappzzang.jeongsan.util.ColorParser

object ExpenseEntityMapper {
    fun mapResponseWithExpenseEntityToModel(entity: ResponseWithExpenseIdDTO): String =
        entity.expenseId.toString()

    fun mapCategoryToModel(entity: CategoryEntity): ExpenseCategory = ExpenseCategory(
        id = entity.id.toString(),
        color = ColorParser.parseColor(entity.color),
        name = entity.name
    )
}
