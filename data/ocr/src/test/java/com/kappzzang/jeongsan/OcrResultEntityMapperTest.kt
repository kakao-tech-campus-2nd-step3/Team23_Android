package com.kappzzang.jeongsan

import com.kappzzang.jeongsan.entity.OcrResultDetailItem
import com.kappzzang.jeongsan.entity.OcrResultEntity
import com.kappzzang.jeongsan.mapper.OcrResultEntityMapper
import com.kappzzang.jeongsan.util.DateConverter.formatToTransferString
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalDateTime

class OcrResultEntityMapperTest {

    @Test
    fun `매핑 된 객체의 Title은 DTO의 Title과 일치해야 한다`() {
        // given
        val entityTitle = "title"

        val entity = OcrResultEntity(
            items = listOf(),
            title = entityTitle,
            paymentTime = LocalDateTime.of(2010, 8, 15, 4, 22, 30).formatToTransferString()
        )

        // when
        val mapped = OcrResultEntityMapper.mapOcrResultEntityToModel(entity)

        // then
        assertThat(mapped.name).isEqualTo(entityTitle)
    }

    @Test
    fun `DTO의 paymentTime은 정확하게 변환되어야 한다`() {
        // given
        val year = 2024
        val month = 7
        val date = 10
        val hour = 13
        val minute = 30
        val second = 32
        val paymentTime = LocalDateTime.of(year, month, date, hour, minute, second) .formatToTransferString()

        val entity = OcrResultEntity(
            items = listOf(),
            title = "title",
            paymentTime = paymentTime
        )

        // when
        val mapped = OcrResultEntityMapper.mapOcrResultEntityToModel(entity)
        val mappedTime = mapped.paymentTime

        // then

        assertThat(mappedTime.year).isEqualTo(year)
        assertThat(mappedTime.monthValue).isEqualTo(month)
        assertThat(mappedTime.dayOfMonth).isEqualTo(date)
        assertThat(mappedTime.hour).isEqualTo(hour)
        assertThat(mappedTime.minute).isEqualTo(minute)
        assertThat(mappedTime.second).isEqualTo(second)
    }

    @Test
    fun `OCR 내 items는 적절하게 매핑되어야 한다`() {
        // given
        val names = listOf("name_1", "name_2")
        val quantities = listOf(10, 0)
        val prices = listOf(20000, 500)
        val items:MutableList<OcrResultDetailItem> = mutableListOf()

        for(i in names.indices){
            items.add(
                OcrResultDetailItem(
                    name = names[i],
                    unitPrice = prices[i],
                    quantity = quantities[i]
                )
            )
        }

        val entity = OcrResultEntity(
            items = items,
            title = "title",
            paymentTime = LocalDateTime.of(2010, 8, 15, 4, 22, 30).formatToTransferString()
        )

        // when
        val mapped = OcrResultEntityMapper.mapOcrResultEntityToModel(entity)

        // then
        mapped.detailItems.forEachIndexed { index, item ->
            assertThat(item.itemName).isEqualTo(names[index])
            assertThat(item.itemPrice).isEqualTo(prices[index])
            assertThat(item.itemQuantity).isEqualTo(quantities[index])
        }
    }
}
