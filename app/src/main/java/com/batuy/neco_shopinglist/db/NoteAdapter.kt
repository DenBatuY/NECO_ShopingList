package com.batuy.neco_shopinglist.db

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.batuy.neco_shopinglist.R
import com.batuy.neco_shopinglist.utils.HTML_Manager
import com.batuy.neco_shopinglist.databinding.NoteListItemBinding
import com.batuy.neco_shopinglist.entities.NoteItem
import com.batuy.neco_shopinglist.utils.TimeManager

class NoteAdapter(private val listener:Listener, private val defPref:SharedPreferences): ListAdapter<NoteItem, NoteAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(view:View):RecyclerView.ViewHolder(view){
        private val binding = NoteListItemBinding.bind(view)
        fun setData(note:NoteItem,listener:Listener, defPref:SharedPreferences){
            binding.tvTitle.text=note.title
            binding.tvDescription.text= HTML_Manager.getFromHtml(note.content).trim()
            binding.tvTime.text=TimeManager.getTimeFormat(note.time, defPref)
            binding.btDelete.setOnClickListener { listener.deleteItem(note.id!!) }// Удаление елемента
            itemView.setOnClickListener { listener.onClickItem(note) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_list_item,parent,false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position),listener,defPref)
    }

    class ItemComparator:DiffUtil.ItemCallback<NoteItem>() {

        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
         return   oldItem==newItem
        }
    }
    interface Listener{
        fun deleteItem(id:Int)
        fun onClickItem(note:NoteItem)
    }
}