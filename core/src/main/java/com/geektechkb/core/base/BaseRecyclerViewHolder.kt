package com.geektechkb.core.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerViewHolder<out V : ViewBinding, in I : Any>(
    val binding: V
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun onBind(item: I): Unit?
}