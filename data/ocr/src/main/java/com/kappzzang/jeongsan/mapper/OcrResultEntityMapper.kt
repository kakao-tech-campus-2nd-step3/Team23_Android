package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.domain.model.OcrResultResponse
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import java.sql.Timestamp

object OcrResultEntityMapper {
    fun mapOcrResultEntityToModel(entity: com.kappzzang.jeongsan.entity.OcrResultEntity) =
        OcrResultResponse.OcrSuccess(
            name = entity.title,
            paymentTime = Timestamp.valueOf(entity.paymentTime),
            detailItems = entity.items.mapIndexed { index, item ->
                com.kappzzang.jeongsan.model.ExpenseDetailItem(
                    id = index.toString(),
                    itemQuantity = item.quantity,
                    itemName = item.name,
                    itemPrice = item.unitPrice,
                    selectedQuantity = 0
                )
            }
        )
}
