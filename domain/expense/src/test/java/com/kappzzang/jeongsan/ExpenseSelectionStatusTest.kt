package com.kappzzang.jeongsan

import com.kappzzang.jeongsan.model.ExpenseSelectionStatusItem
import com.kappzzang.jeongsan.model.ExpenseSelectorInfo
import kotlin.math.floor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ExpenseSelectionStatusTest {
    @Test
    fun `아이템의 TotalPrice는 올바르게 계산된다`() {
        // given
        val quantity = 5
        val unitPrice = 200

        // when
        val item = ExpenseSelectionStatusItem(
            itemId = "",
            name = "",
            quantity = quantity,
            unitPrice = unitPrice,
            selectorList = emptyList()
        )

        // then
        assertThat(item.totalPrice).isEqualTo(quantity * unitPrice)
    }

    private fun createSelector(selectedQuantity: Int) = ExpenseSelectorInfo(
        name = "",
        profileImageUrl = "",
        selectedQuantity = selectedQuantity
    )

    @Test
    fun `인원별 지불액은 올바르게 계산된다`() {
        // given
        val quantity = 5
        val unitPrice = 2000
        val person1selection = 2
        val person2selection = 3
        val person3selection = 5

        // when
        val item = ExpenseSelectionStatusItem(
            itemId = "",
            name = "",
            quantity = quantity,
            unitPrice = unitPrice,
            selectorList = listOf(
                createSelector(person1selection),
                createSelector(person2selection),
                createSelector(person3selection)
            )
        )

        // then
        assertThat(item.getTotalPriceForSelectedQuantity(person1selection)).isEqualTo(2000)
        assertThat(item.getTotalPriceForSelectedQuantity(person2selection)).isEqualTo(3000)
        assertThat(item.getTotalPriceForSelectedQuantity(person3selection)).isEqualTo(5000)
    }

    @Test
    fun `인원별 지불액을 나눌 때 소숫점 아래는 버림한다`() {
        // given
        val quantity = 1
        val unitPrice = 7755
        val person1selection = 1
        val person2selection = 2
        val person3selection = 4

        // when
        val item = ExpenseSelectionStatusItem(
            itemId = "",
            name = "",
            quantity = quantity,
            unitPrice = unitPrice,
            selectorList = listOf(
                createSelector(person1selection),
                createSelector(person2selection),
                createSelector(person3selection)
            )
        )

        // then
        val divided =
            unitPrice.toDouble() / (person1selection + person2selection + person3selection)
        assertThat(item.getTotalPriceForSelectedQuantity(person1selection))
            .isEqualTo(floor(divided * person1selection).toInt())
        assertThat(item.getTotalPriceForSelectedQuantity(person2selection))
            .isEqualTo(floor(divided * person2selection).toInt())
        assertThat(item.getTotalPriceForSelectedQuantity(person3selection))
            .isEqualTo(floor(divided * person3selection).toInt())
    }
}
