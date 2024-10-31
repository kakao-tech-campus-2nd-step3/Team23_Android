package com.kappzzang.jeongsan.expenselist.util

import com.kappzzang.jeongsan.data.ExpenseUiItem
import com.kappzzang.jeongsan.expenselist.viewmodel.ExpenseListPageViewModel.Companion.CURRENCY_POSTFIX
import com.kappzzang.jeongsan.model.ExpenseItem
import com.kappzzang.jeongsan.util.IntegerFormatter.formatDecimalSeparator

object ExpenseUiItemMapper {
    internal fun mapToExpenseUiItem(expenseItemList: List<ExpenseItem>): List<ExpenseUiItem> =
        expenseItemList.mapIndexed { index, item ->
            ExpenseUiItem(
                isFirstItem = index == 0,
                isLastItem = index == expenseItemList.size - 1,
                id = item.id,
                name = item.name,
                date = item.date,
                payerMemberId = item.payerMemberId,
                categoryColor = item.categoryColor,
                payerName = item.payerName,
                price = "${item.price.formatDecimalSeparator()} $CURRENCY_POSTFIX"
            )
        }

    internal fun sortItemsByTime(expenseItemList: List<ExpenseItem>): List<ExpenseItem> =
        expenseItemList.sortedBy {
            it.date
        }.reversed()
}
