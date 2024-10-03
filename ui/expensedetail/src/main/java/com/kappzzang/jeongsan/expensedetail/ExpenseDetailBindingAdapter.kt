package com.kappzzang.jeongsan.expensedetail

import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kappzzang.jeongsan.model.ExpenseDetailItem
import kotlinx.coroutines.flow.StateFlow

object ExpenseDetailBindingAdapter {
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

    @JvmStatic
    @BindingAdapter("app:detail_items")
    fun attachList(
        recyclerView: RecyclerView,
        items: StateFlow<List<ExpenseDetailItem>>?
    ) {
        items?.let {
            (recyclerView.adapter as? ExpenseDetailItemListAdapter)?.submitList(it.value)
        }
    }

    @JvmStatic
    @BindingAdapter("app:detail_selection")
    fun attachList(view: AutoCompleteTextView, position: Int) {
        if (position < 0 || position >= view.adapter.count) {
            view.setText("0", false)
            return
        }
        view.setText(view.adapter.getItem(position).toString(), false)
    }
}
