package com.batuy.neco_shopinglist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.batuy.neco_shopinglist.R
import com.batuy.neco_shopinglist.databinding.ListNameItemBinding
import com.batuy.neco_shopinglist.entities.ShopListNameItem

class ShopListNameAdapter(private val listener:Listener): ListAdapter<ShopListNameItem, ShopListNameAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(view:View):RecyclerView.ViewHolder(view){
        private val binding = ListNameItemBinding.bind(view)


        fun setData(shopListNameItem:ShopListNameItem, listener: Listener){
            binding.tvListName.text=shopListNameItem.name
            binding.tvTime.text=shopListNameItem.time
            binding.imDelete.setOnClickListener { listener.deleteItem(shopListNameItem.id!!) }// Удаление елемента
            itemView.setOnClickListener { listener.onClickItem(shopListNameItem)}
            binding.imEdite.setOnClickListener { listener.editAtem(shopListNameItem) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_name_item,parent,false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position),listener)
    }

    class ItemComparator:DiffUtil.ItemCallback<ShopListNameItem>() {

        override fun areItemsTheSame(oldItem: ShopListNameItem, newItem: ShopListNameItem): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopListNameItem, newItem: ShopListNameItem): Boolean {
         return   oldItem==newItem
        }
    }
    interface Listener{
        fun deleteItem(id:Int)
        fun editAtem(shopListName:ShopListNameItem)
        fun onClickItem(shopListName:ShopListNameItem)
    }
}