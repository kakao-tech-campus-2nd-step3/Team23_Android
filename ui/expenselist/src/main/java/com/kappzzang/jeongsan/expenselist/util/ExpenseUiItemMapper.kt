package com.kappzzang.jeongsan.expenselist.util

import com.kappzzang.jeongsan.data.ExpenseUiItem
import com.kappzzang.jeongsan.expenselist.viewmodel.ExpenseListPageViewModel.Companion.CURRENCY_POSTFIX
import com.kappzzang.jeongsan.expenselist.viewmodel.ExpenseListPageViewModel.Companion.MY_EXPENSE_PREFIX
import com.kappzzang.jeongsan.model.ExpenseItemWithCategory
import com.kappzzang.jeongsan.model.ExpenseState
import com.kappzzang.jeongsan.util.ColorParser
import com.kappzzang.jeongsan.util.IntegerFormatter.formatDecimalSeparator

object ExpenseUiItemMapper {
    internal fun mapToExpenseUiItem(
        expenseItemList: List<ExpenseItemWithCategory>
    ): List<ExpenseUiItem> = expenseItemList.mapIndexed { index, item ->
        ExpenseUiItem(
            isFirstItem = index == 0,
            isLastItem = index == expenseItemList.size - 1,
            id = item.id,
            name = item.name,
            date = item.date,
            categoryColor = ColorParser.parseColor(item.categoryColor),
            price = "${item.price.formatDecimalSeparator()} $CURRENCY_POSTFIX",
            isMyPayment = item.isMyPayment,
            indicateNotConfirmedDot = item.state == ExpenseState.NOT_CONFIRMED,
            indicateMyPrice = (item.state == ExpenseState.TRANSFERED || item.state == ExpenseState.TRANSFER_PENDING) && item.personalExpense != null,
            myPrice = item.personalExpense?.let {
                "$MY_EXPENSE_PREFIX ${it.formatDecimalSeparator()} $CURRENCY_POSTFIX"
            } ?: ""
        )
    }

    internal fun sortItemsByTime(
        expenseItemList: List<ExpenseItemWithCategory>
    ): List<ExpenseItemWithCategory> = expenseItemList.sortedByDescending {
        it.date
    }
}
