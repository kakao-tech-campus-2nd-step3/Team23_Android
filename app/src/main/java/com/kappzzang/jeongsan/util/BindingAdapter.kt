package com.kappzzang.jeongsan.util

import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kappzzang.jeongsan.expenselist.ExpenseListAdapter
import com.kappzzang.jeongsan.expenselist.ExpenseListViewUIData
import kotlinx.coroutines.flow.StateFlow

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("expenseImageUrl")
    fun loadImage(view: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(url)
                .into(view)
        } else {
            // 기본 이미지 또는 처리 로직 추가 가능
        }
    }

    @BindingAdapter("stringText")
    @JvmStatic
    fun setTextValue(view: EditText, value: String?) {
        view.setText(value ?: "")
    }

    @InverseBindingAdapter(attribute = "stringText")
    @JvmStatic
    fun getTextValue(view: EditText): String? {
        val parsedString = view.text.toString()
        if (parsedString.isBlank()) {
            return null
        }
        return parsedString
    }

    @BindingAdapter("integerText")
    @JvmStatic
    fun setIntegerValue(view: EditText, value: Int?) {
        view.setText(value?.toString() ?: "")
    }

    @InverseBindingAdapter(attribute = "integerText")
    @JvmStatic
    fun getIntegerValue(view: EditText): Int? = view.text.toString().toIntOrNull()

    @BindingAdapter("integerTextAttrChanged")
    @JvmStatic
    fun setIntegerTextListener(view: EditText, listener: InverseBindingListener) {
        view.addTextChangedListener {
            listener.onChange()
        }
    }

    @BindingAdapter("stringTextAttrChanged")
    @JvmStatic
    fun setStringTextListener(view: EditText, listener: InverseBindingListener) {
        view.addTextChangedListener {
            listener.onChange()
        }
    }

    @BindingAdapter("expenseItems")
    @JvmStatic
    fun attachExpenseList(
        recyclerView: RecyclerView,
        items: StateFlow<com.kappzzang.jeongsan.expenselist.ExpenseListViewUIData>?
    ) {
        items?.let {
            (recyclerView.adapter as? com.kappzzang.jeongsan.expenselist.ExpenseListAdapter)
                ?.submitList(
                    it.value.expenseItems
                )
        }
    }
}
