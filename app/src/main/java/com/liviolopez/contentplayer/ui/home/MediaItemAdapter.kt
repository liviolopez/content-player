package com.liviolopez.contentplayer.ui.home

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
import com.liviolopez.contentplayer.utils.extensions.visibleIf

class MediaItemAdapter(
    private val clickListener: (Item) -> Unit
) : ListAdapter<Item, BindingViewHolder<ViewBinding>>(ItemComparator()) {

    override fun getItemViewType(position: Int): Int {
        /** TODO() Implement viewHolder for audio **/
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

    @JvmName("bindItemVideoBinding")
    private fun BindingViewHolder<ItemVideoBinding>.bind(item: Item) {

        binding.apply {
            tvFormat.text = item.format.uppercase()
            tvUrl.text = item.url

            tvDrmUuid.text = item.drmUuid?.uppercase() ?: "without content protection"
            ivDrmUuid.visibleIf { !item.drmUuid.isNullOrEmpty() }

            tvDrmLicense.text = item.drmLicense
            ivDrmLicense.visibleIf { !item.drmLicense.isNullOrEmpty() }

            root.setOnClickListener {
                this@MediaItemAdapter.clickListener(item)
            }
        }
    }

    private fun BindingViewHolder<ItemAudioBinding>.bind(item: Item) {
        binding.apply { /** TODO() Implement viewHolder for audio **/ }
    }
}