package com.kappzzang.jeongsan.expenselist.customview

import android.graphics.Outline
import android.util.TypedValue
import android.view.View
import android.view.ViewOutlineProvider

enum class ExpenseListItemBoxType { TOP_CORNER, BOTTOM_CORNER, ALL_CORNERS, NO_CORNERS }

class CustomOutlineProvider(
    private val cornerRadiusDP: Float,
    private val outlineType: ExpenseListItemBoxType
) : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        val left = 0
        val top = 0
        val right = view.width
        val bottom = view.height

        val cornerRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            cornerRadiusDP,
            view.resources.displayMetrics
        )
        when (outlineType) {
            ExpenseListItemBoxType.TOP_CORNER ->
                outline.setRoundRect(
                    left, top, right,
                    bottom + cornerRadius.toInt(), cornerRadius
                )

            ExpenseListItemBoxType.BOTTOM_CORNER ->
                outline.setRoundRect(
                    left,
                    top - cornerRadius.toInt(),
                    right,
                    bottom,
                    cornerRadius
                )

            ExpenseListItemBoxType.NO_CORNERS ->
                outline.setRoundRect(
                    left,
                    top,
                    right,
                    bottom,
                    0f
                )

            ExpenseListItemBoxType.ALL_CORNERS ->
                outline.setRoundRect(
                    left,
                    top,
                    right,
                    bottom,
                    cornerRadius
                )
        }
    }
}
