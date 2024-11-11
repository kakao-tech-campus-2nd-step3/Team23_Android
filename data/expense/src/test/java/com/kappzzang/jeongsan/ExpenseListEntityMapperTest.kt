package com.kappzzang.jeongsan

import com.kappzzang.jeongsan.entity.expenselist.CategoryEntity
import com.kappzzang.jeongsan.entity.expenselist.ExpenseRemoteEntity
import com.kappzzang.jeongsan.mapper.ExpenseListEntityMapper
import com.kappzzang.jeongsan.model.ExpenseState
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ExpenseListEntityMapperTest {
    private fun getSampleEntity() = ExpenseRemoteEntity(
        payerServiceId = 0,
        category = CategoryEntity(
            name = "category",
            color = "ffffff"
        ),
        checked = null,
        myExpense = 1000,
        id = 100L,
        title = "name",
        state = "ongoing",
        createdAt = "2000-04-16 20:11:00",
        totalPrice = 12500
    )

    @Test
    fun `엔티티를 매핑 후 시간 값이 정상적으로 변형된다`() {
        // given
        val entity = getSampleEntity()
        val serviceId = "0"

        // when
        val mapped = ExpenseListEntityMapper.mapExpenseEntityToModel(entity, serviceId)

        // then
        assertThat(mapped.date.year).isEqualTo(2000)
        assertThat(mapped.date.monthValue).isEqualTo(4)
        assertThat(mapped.date.dayOfMonth).isEqualTo(16)
        assertThat(mapped.date.hour).isEqualTo(20)
        assertThat(mapped.date.minute).isEqualTo(11)
        assertThat(mapped.date.second).isEqualTo(0)
    }

    @Test
    fun `엔티티를 매핑 후 색상 값이 정상적으로 변형된다`() {
        // given
        val testColorCode1 = "ffdd00"
        val expectedColorCode1 = "#ffdd00"

        val testColorCode2 = "#FFAABB"
        val expectedColorCode2 = "#ffaabb"
        val serviceId = "0"

        val entity1 = getSampleEntity().copy(
            category = CategoryEntity(
                name = "testCategory",
                color = testColorCode1
            )
        )
        val entity2 = getSampleEntity().copy(
            category = CategoryEntity(
                name = "testCategory",
                color = testColorCode2
            )
        )

        // when
        val mapped1 = ExpenseListEntityMapper.mapExpenseEntityToModel(entity1, serviceId)
        val mapped2 = ExpenseListEntityMapper.mapExpenseEntityToModel(entity2, serviceId)

        // then
        assertThat(mapped1.categoryColor).isEqualTo(expectedColorCode1)
        assertThat(mapped2.categoryColor).isEqualTo(expectedColorCode2)
    }

    @Test
    fun `확인되지 않은 지출이 ExpenseState로 정상적으로 매핑된다`() {
        // given
        val serverState = "ongoing"
        val serverChecked = false
        val serviceId = "0"

        val expectedDomainState = ExpenseState.NOT_CONFIRMED

        val entity = getSampleEntity().copy(
            checked = serverChecked,
            state = serverState
        )

        // when
        val mapped = ExpenseListEntityMapper.mapExpenseEntityToModel(entity, serviceId, serverChecked)

        // then
        assertThat(mapped.state).isEqualTo(expectedDomainState)
    }

    @Test
    fun `확인된 않은 지출이 ExpenseState로 정상적으로 매핑된다`() {
        // given
        val serverState = "ongoing"
        val serverChecked = true
        val serviceId = "0"

        val expectedDomainState = ExpenseState.CONFIRMED

        val entity = getSampleEntity().copy(
            checked = serverChecked,
            state = serverState
        )

        // when
        val mapped = ExpenseListEntityMapper.mapExpenseEntityToModel(entity, serviceId, serverChecked)

        // then
        assertThat(mapped.state).isEqualTo(expectedDomainState)
    }

    @Test
    fun `송금 대기중인 지출이 ExpenseState로 정상적으로 매핑된다`() {
        // given
        val serverState = "pending"
        val serviceId = "0"

        val expectedDomainState = ExpenseState.TRANSFER_PENDING

        val entity = getSampleEntity().copy(
            state = serverState
        )

        // when
        val mapped = ExpenseListEntityMapper.mapExpenseEntityToModel(entity, serviceId)

        // then
        assertThat(mapped.state).isEqualTo(expectedDomainState)
    }

    @Test
    fun `송금 완료된 지출이 ExpenseState로 정상적으로 매핑된다`() {
        // given
        val serverState = "completed"
        val serviceId = "0"

        val expectedDomainState = ExpenseState.TRANSFERED

        val entity = getSampleEntity().copy(
            state = serverState
        )

        // when
        val mapped = ExpenseListEntityMapper.mapExpenseEntityToModel(entity, serviceId)

        // then
        assertThat(mapped.state).isEqualTo(expectedDomainState)
    }
}
