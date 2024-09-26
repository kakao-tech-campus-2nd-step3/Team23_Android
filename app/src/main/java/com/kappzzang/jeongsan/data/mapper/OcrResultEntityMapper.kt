package com.kappzzang.jeongsan.data.mapper

import com.kappzzang.jeongsan.data.entity.OcrResultEntity
import com.kappzzang.jeongsan.domain.model.ExpenseDetailItem
import com.kappzzang.jeongsan.domain.model.OcrResultResponse
import java.sql.Timestamp

object OcrResultEntityMapper {
    fun mapOcrResultEntityToModel (entity: OcrResultEntity) = OcrResultResponse.OcrSuccess(
        name = entity.title,
        paymentTime = Timestamp.valueOf(entity.paymentTime),
        detailItems = entity.items.mapIndexed { index, item ->
            ExpenseDetailItem(
                id = index.toString(),
                itemQuantity = item.quantity,
                itemName = item.name,
                itemPrice = item.unitPrice,
                selectedQuantity = 0
            )
        }
    )
}
