package com.kappzzang.jeongsan.expensedetail.selectionstatus

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kappzzang.jeongsan.data.SelectionInfoItem
import com.kappzzang.jeongsan.expensedetail.databinding.ItemDividerBinding
import com.kappzzang.jeongsan.expensedetail.databinding.ItemSelectionItemHeaderBinding
import com.kappzzang.jeongsan.expensedetail.databinding.ItemSelectorInfoBinding

class SelectionStatusItemListAdapter(internal val context: Context) :
    ListAdapter<SelectionInfoItem, SelectionStatusItemListAdapter.SelectionStatusItemViewHolder>(
        object :
            DiffUtil.ItemCallback<SelectionInfoItem>() {
            override fun areItemsTheSame(
                oldItem: SelectionInfoItem,
                newItem: SelectionInfoItem
            ): Boolean = when (oldItem) {
                SelectionInfoItem.Divider -> {
                    newItem is SelectionInfoItem.Divider
                }

                is SelectionInfoItem.Header -> {
                    (newItem as? SelectionInfoItem.Header)?.let {
                        oldItem.name == newItem.name
                    } ?: let {
                        false
                    }
                }

                is SelectionInfoItem.SelectorItem -> {
                    (newItem as? SelectionInfoItem.SelectorItem)?.let {
                        oldItem.name == newItem.name && oldItem.imageUrl == newItem.imageUrl
                    } ?: let {
                        false
                    }
                }
            }

            override fun areContentsTheSame(
                oldItem: SelectionInfoItem,
                newItem: SelectionInfoItem
            ): Boolean = oldItem == newItem
        }
    ) {
    class SelectionStatusItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var headerBinding: ItemSelectionItemHeaderBinding? = null
        private var selectorInfoBinding: ItemSelectorInfoBinding? = null

        constructor(headerBinding: ItemSelectionItemHeaderBinding) : this(headerBinding.root) {
            this.headerBinding = headerBinding
        }

        constructor(selectorInfoBinding: ItemSelectorInfoBinding) : this(selectorInfoBinding.root) {
            this.selectorInfoBinding = selectorInfoBinding
        }

        constructor(dividerBinding: ItemDividerBinding) : this(dividerBinding.root) {}

        fun bindHeader(header: SelectionInfoItem.Header) {
            headerBinding?.apply {
                this.header = header
            }
        }

        fun bindSelectorInfo(item: SelectionInfoItem.SelectorItem) {
            selectorInfoBinding?.apply {
                this.item = item

                if ((bindingAdapter as? SelectionStatusItemListAdapter) == null) {
                    return
                }
                Glide.with((bindingAdapter as SelectionStatusItemListAdapter).context)
                    .load(item.imageUrl)
                    .circleCrop()
                    .into(expenseSelectionThumbnailImageview)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectionStatusItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            CELL_HEADER -> {
                return SelectionStatusItemViewHolder(
                    ItemSelectionItemHeaderBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            }

            CELL_ITEM -> {
                return SelectionStatusItemViewHolder(
                    ItemSelectorInfoBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            }

            CELL_DIVIDER -> {
                return SelectionStatusItemViewHolder(
                    ItemDividerBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            }

            else -> {
                throw IllegalStateException("잘못된 View Type 입니다.")
            }
        }
    }

    override fun onBindViewHolder(holder: SelectionStatusItemViewHolder, position: Int) {
        when (holder.itemViewType) {
            CELL_HEADER -> {
                (getItem(position) as? SelectionInfoItem.Header)?.let {
                    holder.bindHeader(it)
                }
            }

            CELL_ITEM -> {
                (getItem(position) as? SelectionInfoItem.SelectorItem)?.let {
                    holder.bindSelectorInfo(it)
                }
            }

            CELL_DIVIDER -> {
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        SelectionInfoItem.Divider -> CELL_DIVIDER
        is SelectionInfoItem.Header -> CELL_HEADER
        is SelectionInfoItem.SelectorItem -> CELL_ITEM
    }

    companion object {
        const val CELL_HEADER = 0
        const val CELL_ITEM = 1
        const val CELL_DIVIDER = 2
    }
}
