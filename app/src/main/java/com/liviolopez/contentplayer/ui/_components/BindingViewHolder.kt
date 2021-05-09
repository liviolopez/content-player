package com.liviolopez.contentplayer.ui._components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * This code snippet was taken from the Android Extension Samples.
 * https://github.com/tunjid/Android-Extensions
 */

open class BindingViewHolder<T : ViewBinding> private constructor(
    val binding: T
) : RecyclerView.ViewHolder(binding.root) {
    constructor(
        parent: ViewGroup,
        creator: (inflater: LayoutInflater, root: ViewGroup, attachToRoot: Boolean) -> T
    ) : this(
        creator(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )
}

fun <T : ViewBinding> ViewGroup.viewHolderFrom(
    creator: (inflater: LayoutInflater, root: ViewGroup, attachToRoot: Boolean) -> T
): BindingViewHolder<T> = BindingViewHolder(this, creator)

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewBinding> BindingViewHolder<*>.typed() = this as BindingViewHolder<T>