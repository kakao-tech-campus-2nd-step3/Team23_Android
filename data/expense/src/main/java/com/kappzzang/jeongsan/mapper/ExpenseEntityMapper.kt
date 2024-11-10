package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.CategoryEntity
import com.kappzzang.jeongsan.entity.ResponseWithExpenseIdDTO
import com.kappzzang.jeongsan.model.ExpenseCategory
import java.util.regex.Pattern

object ExpenseEntityMapper {
    private val HEXADECIMAL_PATTERN: Pattern = Pattern.compile("\\p{XDigit}+")

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

    fun mapResponseWithExpenseEntityToModel(entity: ResponseWithExpenseIdDTO): String =
        entity.expenseId

    fun mapCategoryToModel(entity: CategoryEntity): ExpenseCategory = ExpenseCategory(
        id = entity.id.toString(),
        color = parseColor(entity.color),
        name = entity.name
    )
}
