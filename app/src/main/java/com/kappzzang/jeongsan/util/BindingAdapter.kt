package com.kappzzang.jeongsan.util

import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

object BindingAdapter {
    @BindingAdapter("stringText")
    @JvmStatic
    fun setTextValue(view: EditText, value: String?) {
        view.setText(value?:"")
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
        view.setText(value?.toString()?:"")
    }

    @InverseBindingAdapter(attribute = "integerText")
    @JvmStatic
    fun getIntegerValue(view: EditText): Int? {
        return view.text.toString().toIntOrNull()
    }

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
}