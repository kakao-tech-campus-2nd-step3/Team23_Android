package com.kappzzang.jeongsan

import com.kappzzang.jeongsan.entity.ExpenseEntity
import com.kappzzang.jeongsan.mapper.ExpenseEntityMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ExpenseEntityMapperTest {
    private fun getSampleEntity() = ExpenseEntity(
        id = 100,
        name = "name",
        expenseState = 0,
        createdTime = "2000-04-16 20:11:00",
        image = "https://example.org",
        categoryName = "categoryName",
        categoryColor = "#ffaa00",
        totalPrice = 12500
    )

    @Test
    fun `엔티티를 매핑 후 시간 값이 정상적으로 변형된다`() {
        //given
        val entity = getSampleEntity()

        //when
        val mapped = ExpenseEntityMapper.mapExpenseEntityToModel(entity)

        //then
        assertThat(mapped.date.year).isEqualTo(2000)
        assertThat(mapped.date.monthValue).isEqualTo(4)
        assertThat(mapped.date.dayOfMonth).isEqualTo(16)
        assertThat(mapped.date.hour).isEqualTo(20)
        assertThat(mapped.date.minute).isEqualTo(11)
        assertThat(mapped.date.second).isEqualTo(0)
    }
}
