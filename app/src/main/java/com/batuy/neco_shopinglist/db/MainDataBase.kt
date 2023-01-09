package com.batuy.neco_shopinglist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.batuy.neco_shopinglist.entities.LibraryItem
import com.batuy.neco_shopinglist.entities.NoteItem
import com.batuy.neco_shopinglist.entities.ShopingListItem
import com.batuy.neco_shopinglist.entities.ShopListNameItem

@Database(entities = [LibraryItem::class,NoteItem::class,ShopingListItem::class,ShopListNameItem::class], version = 1)

abstract class MainDataBase: RoomDatabase() {
    abstract fun getDao():Dao
    companion object{
        @Volatile
        private var INSTANCE:MainDataBase? = null

        fun getDataBase(context: Context):MainDataBase{
            return INSTANCE ?: synchronized(this){
                val instance= Room.databaseBuilder(context.applicationContext,
                MainDataBase::class.java,"shopping_list.db").build()
                instance
            }
        }
    }
}