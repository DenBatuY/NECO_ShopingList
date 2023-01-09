package com.batuy.neco_shopinglist.db

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.batuy.neco_shopinglist.R
import com.batuy.neco_shopinglist.databinding.ShopListItemBinding
import com.batuy.neco_shopinglist.entities.ShopingListItem

class ShopListItemAdapter(private val listener: Listener) :
    ListAdapter<ShopingListItem, ShopListItemAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun setItemData(shopListItem: ShopingListItem, listener: Listener) {
            val binding = ShopListItemBinding.bind(view)
            binding.tvName.text = shopListItem.name
            binding.tvInfo.text = shopListItem.itemInfo
            binding.tvInfo.visibility = infoVisibility(shopListItem)
            binding.checkBox.isChecked = shopListItem.itemChecked
            setPaintFlagAndColor(binding)
            binding.checkBox.setOnClickListener {
                listener.onClickItem(shopListItem.copy(itemChecked = binding.checkBox.isChecked),
                    CHECK_BOX)
            }
            binding.imEdit.setOnClickListener { listener.onClickItem(shopListItem, EDIT) }
        }

        fun setLibraryData(shopListNameItem: ShopingListItem, listener: Listener) {}

        private fun infoVisibility(shopListNameItem: ShopingListItem): Int {
            return if (shopListNameItem.itemInfo.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        private fun setPaintFlagAndColor(binding: ShopListItemBinding) {
            if (binding.checkBox.isChecked) {
                binding.tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                binding.tvInfo.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                binding.tvName.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.grey
                    )
                )

            } else {
                binding.tvName.paintFlags = Paint.ANTI_ALIAS_FLAG
                binding.tvInfo.paintFlags = Paint.ANTI_ALIAS_FLAG
                binding.tvName.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.black
                    )
                )
            }
        }

        companion object {
            fun createShopItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.shop_list_item, parent, false)
                )
            }

            fun createLibraryItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.shop_library_list_item, parent, false)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {

        return if (viewType == 0) ItemHolder.createShopItem(parent)
        else ItemHolder.createLibraryItem(parent)
    }


    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (getItem(position).itemType == 0) holder.setItemData(getItem(position), listener)
        else holder.setLibraryData(getItem(position), listener)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    class ItemComparator : DiffUtil.ItemCallback<ShopingListItem>() {

        override fun areItemsTheSame(oldItem: ShopingListItem, newItem: ShopingListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ShopingListItem,
            newItem: ShopingListItem
        ): Boolean {
            return oldItem == newItem
        }

        companion object {
            fun createShopItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.shop_list_item, parent, false)
                )
            }

            fun createLibraryItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.shop_library_list_item, parent, false)
                )
            }
        }
    }

    interface Listener {
        fun onClickItem(shopListItem: ShopingListItem, state:Int)
    }

    companion object{
        const val EDIT = 0
        const val CHECK_BOX = 1
    }


}