package com.monke.machnomusic3.ui.recyclerViewUtils

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback<T> (
    private val oldList: List<T>,
    private val newList: List<T>,
    private val areItemsSame: (oldItem: T, newItem: T) -> Boolean,
    private val areContentsSame: (oldItem: T, newItem: T) -> Boolean,
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        areItemsSame(oldList[oldItemPosition], newList[newItemPosition],)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        areContentsSame(oldList[oldItemPosition], newList[newItemPosition],)

}