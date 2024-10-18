package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.OcrResultEntity
import com.kappzzang.jeongsan.model.OcrDetailItem
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.util.DateConverter

object OcrResultEntityMapper {
    fun mapOcrResultEntityToModel(entity: OcrResultEntity) = OcrResultResponse.OcrSuccess(
        name = entity.title,
        paymentTime = DateConverter.parseFromString(entity.paymentTime),
        detailItems = entity.items.map { item ->
            OcrDetailItem(
                itemName = item.name,
                itemPrice = item.unitPrice,
                itemQuantity = item.quantity
            )
        }
    )
}
