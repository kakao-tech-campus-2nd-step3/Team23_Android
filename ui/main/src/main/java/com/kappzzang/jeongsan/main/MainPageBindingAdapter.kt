package com.kappzzang.jeongsan.main

import android.widget.ImageView
import androidx.databinding.BindingAdapter

object MainPageBindingAdapter {

    @JvmStatic
    @BindingAdapter("app:tintColor")
    fun setTintColor(view: ImageView, color: Int) {
        view.setColorFilter(color)
    }
}