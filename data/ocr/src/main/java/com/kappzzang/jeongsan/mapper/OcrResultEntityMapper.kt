package com.kappzzang.jeongsan.mapper

import com.kappzzang.jeongsan.entity.OcrResultDetailItem
import com.kappzzang.jeongsan.entity.OcrResultEntity
import com.kappzzang.jeongsan.entity.ReceiptAnalyzeResponse
import com.kappzzang.jeongsan.model.OcrDetailItem
import com.kappzzang.jeongsan.model.OcrResultResponse
import com.kappzzang.jeongsan.util.DateConverter
import java.time.LocalDateTime

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

    fun mapOcrResultEntityToModel(entity: ReceiptAnalyzeResponse) = OcrResultResponse.OcrSuccess(
        name = entity.title ?: "",
        paymentTime = entity.paymentTime?.let { DateConverter.parseFromString(it) }
            ?: LocalDateTime.now(),
        detailItems = entity.items.map { mapOcrDetailItemEntityToModel(it) }
    )

    private fun mapOcrDetailItemEntityToModel(item: OcrResultDetailItem): OcrDetailItem =
        OcrDetailItem(
            itemQuantity = item.quantity,
            itemPrice = item.unitPrice,
            itemName = item.name
        )
}
