package com.kappzzang.jeongsan

import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailEntity
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseDetailItemEntity
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseSelectionItemEntity
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseSelectionResponseDTO
import com.kappzzang.jeongsan.entity.expensedetail.ExpenseSelectorEntity
import com.kappzzang.jeongsan.mapper.ExpenseDetailMapper
import com.kappzzang.jeongsan.model.ExpenseSelectionStatusItem
import com.kappzzang.jeongsan.model.ExpenseSelectorInfo
import com.kappzzang.jeongsan.model.ExpenseState
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ExpenseDetailEntityMapperTest {
    @Test
    fun `지출 상세내역을 매핑할 때 모든 정보가 정상적으로 매핑된다`() {
        // given
        val expenseTitle = "TITLE"
        val item1UnitPrice = 1000
        val expenseUrl = "https://example.org"
        val item1name = "item1"
        val item1Id = 1L
        val item1Quantity = 2
        val item1QuantityConsumed = 1

        val item2UnitPrice = 1500
        val item2name = "item2"
        val item2Id = 2L
        val item2Quantity = 10
        val item2QuantityConsumed = 4

        val itemEntityList = listOf(
            ExpenseDetailItemEntity(
                unitPrice = item1UnitPrice,
                name = item1name,
                id = item1Id,
                quantity = item1Quantity,
                quantityConsumed = item1QuantityConsumed
            ),
            ExpenseDetailItemEntity(
                unitPrice = item2UnitPrice,
                name = item2name,
                id = item2Id,
                quantity = item2Quantity,
                quantityConsumed = item2QuantityConsumed
            )
        )

        val entity = ExpenseDetailEntity(
            imageUrl = expenseUrl,
            title = expenseTitle,
            detailItems = itemEntityList
        )

        // when
        val mapped = ExpenseDetailMapper.mapDetailedExpenseEntityToModel(
            entity,
            "0",
            ExpenseState.NOT_CONFIRMED
        )

        // then
        assertThat(mapped.name).isEqualTo(expenseTitle)
        assertThat(mapped.expenseImageUrl).isEqualTo(expenseUrl)

        assertThat(mapped.expenseDetails[0].id).isEqualTo(item1Id.toString())
        assertThat(mapped.expenseDetails[0].itemQuantity).isEqualTo(item1Quantity)
        assertThat(mapped.expenseDetails[0].itemName).isEqualTo(item1name)
        assertThat(mapped.expenseDetails[0].itemPrice).isEqualTo(item1UnitPrice)
        assertThat(mapped.expenseDetails[0].selectedQuantity).isEqualTo(item1QuantityConsumed)

        assertThat(mapped.expenseDetails[1].id).isEqualTo(item2Id.toString())
        assertThat(mapped.expenseDetails[1].itemQuantity).isEqualTo(item2Quantity)
        assertThat(mapped.expenseDetails[1].itemName).isEqualTo(item2name)
        assertThat(mapped.expenseDetails[1].itemPrice).isEqualTo(item2UnitPrice)
        assertThat(mapped.expenseDetails[1].selectedQuantity).isEqualTo(item2QuantityConsumed)
    }

    @Test
    fun `지출 상세내역을 매핑할 때 함께 포함한 지출 ID와 지출 상태를 포함한다`() {
        // given
        val expenseId = "id00"
        val expenseState = ExpenseState.TRANSFER_PENDING

        val entity = ExpenseDetailEntity(
            title = "title",
            imageUrl = "url",
            detailItems = listOf()
        )

        // when
        val mapped = ExpenseDetailMapper.mapDetailedExpenseEntityToModel(
            entity = entity,
            expenseId = expenseId,
            state = expenseState
        )

        // then
        assertThat(mapped.id).isEqualTo(expenseId)
        assertThat(mapped.state).isEqualTo(expenseState)
    }

    @Test
    fun `지출 선택 정보를 매핑할 때 모든 정보가 정상적으로 매핑된다`() {
        // given
        val firstSelectionSelectorInfoList = listOf(
            ExpenseSelectorInfo(
                name = "1stSelectorName1",
                selectedQuantity = 4,
                profileImageUrl = "url1"
            ),
            ExpenseSelectorInfo(
                name = "1stSelectorName2",
                selectedQuantity = 4,
                profileImageUrl = "url2"
            )
        )
        val secondSelectionSelectorInfoList = listOf(
            ExpenseSelectorInfo(
                name = "2ndSelectorName1",
                selectedQuantity = 100,
                profileImageUrl = "url3"
            ),
            ExpenseSelectorInfo(
                name = "2ndSelectorName2",
                selectedQuantity = 0,
                profileImageUrl = "url4"
            )
        )

        val firstSelection = ExpenseSelectionStatusItem(
            quantity = 4,
            unitPrice = 100,
            name = "firstSelection",
            itemId = "1",
            selectorList = firstSelectionSelectorInfoList
        )
        val secondSelection = ExpenseSelectionStatusItem(
            quantity = 10,
            unitPrice = 3000,
            name = "secondSelection",
            itemId = "2",
            selectorList = secondSelectionSelectorInfoList
        )

        val expenseSelectionList = listOf(firstSelection, secondSelection)

        val selectionStatusName = "statusName"
        val entity = ExpenseSelectionResponseDTO(
            imageUrl = "",
            title = selectionStatusName,
            items = expenseSelectionList.map {
                ExpenseSelectionItemEntity(
                    id = it.itemId.toInt(),
                    name = it.name,
                    quantity = it.quantity,
                    unitPrice = it.unitPrice,
                    selectorList = it.selectorList.map { selector ->
                        ExpenseSelectorEntity(
                            selectedQuantity = selector.selectedQuantity,
                            name = selector.name,
                            profileImage = selector.profileImageUrl
                        )
                    }
                )
            }
        )

        // when
        val mapped = ExpenseDetailMapper.mapExpenseSelectionStatusEntityToModel(entity)

        // then
        assertThat(mapped.name).isEqualTo(selectionStatusName)

        for (i in expenseSelectionList.indices) {
            val mappedItem = mapped.items[i]
            val expectedItem = expenseSelectionList[i]
            assertThat(mappedItem).isEqualTo(expectedItem)
        }
    }
}
