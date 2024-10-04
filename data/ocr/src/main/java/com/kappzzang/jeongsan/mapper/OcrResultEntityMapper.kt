package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.OcrResultEntity
import com.kappzzang.jeongsan.model.OcrDetailItem
import com.kappzzang.jeongsan.model.OcrResultResponse
import java.sql.Timestamp

object OcrResultEntityMapper {
    fun mapOcrResultEntityToModel(entity: OcrResultEntity) = OcrResultResponse.OcrSuccess(
        name = entity.title,
        paymentTime = Timestamp.valueOf(entity.paymentTime),
        detailItems = entity.items.map { item ->
            OcrDetailItem(
                itemName = item.name,
                itemPrice = item.unitPrice,
                itemQuantity = item.quantity
            )
        }
    )
}
