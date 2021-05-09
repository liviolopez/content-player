package com.liviolopez.contentplayer.ui.home

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.liviolopez.contentplayer.data.local.model.Item
import com.liviolopez.contentplayer.databinding.ItemAudioBinding
import com.liviolopez.contentplayer.databinding.ItemVideoBinding
import com.liviolopez.contentplayer.ui._components.BindingViewHolder
import com.liviolopez.contentplayer.ui._components.typed
import com.liviolopez.contentplayer.ui._components.viewHolderFrom
import com.liviolopez.contentplayer.utils.extensions.setImage

class HomeAdapter(
    private val onItemEventListener: OnItemEventListener? = null,
) : ListAdapter<Item, BindingViewHolder<ViewBinding>>(ItemComparator()) {

    interface OnItemEventListener {
        fun onClick(itemId: String, view: View)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is Item) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ViewBinding> {
        return when(viewType){
            1    -> parent.viewHolderFrom(ItemVideoBinding::inflate)
            else -> parent.viewHolderFrom(ItemAudioBinding::inflate)
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ViewBinding>, position: Int) {
        val item = getItem(position)


        when (getItemViewType(position)) {
            1    -> holder.typed<ItemVideoBinding>().bind(item)
            else -> holder.typed<ItemAudioBinding>().bind(item)
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
    }

    private fun onClick(itemId: String, vararg views: View) {
        views.forEach { view ->
            view.setOnClickListener { onItemEventListener?.onClick(itemId, view) }
        }
    }

    @JvmName("bindItemOnGroceryAllBinding")
    private fun BindingViewHolder<ItemVideoBinding>.bind(item: Item) {
        binding.apply {
            ivThumbnail.setImage(item.thumbnail)
            tvName.text = item.name
        }
    }

    private fun BindingViewHolder<ItemAudioBinding>.bind(item: Item) {
        binding.apply {
            ivThumbnail.setImage(item.thumbnail)
            tvName.text = item.name
        }
    }
}