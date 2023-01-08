package com.batuy.neco_shopinglist.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.batuy.neco_shopinglist.entities.NoteItem
import com.batuy.neco_shopinglist.entities.ShopListNameItem
import com.batuy.neco_shopinglist.entities.ShopingListItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TrainingViewModel(application: Application) : AndroidViewModel(application) {
   private val database = MainDataBase.getDataBase(application)
   private val dao = database.getDao()


    val allNotes: LiveData<List<NoteItem>> = dao.getAllItem().asLiveData()
    val allShoppingListNames: LiveData<List<ShopListNameItem>> = dao.allShoppingListNames().asLiveData()

    fun insertNote(note: NoteItem): Job {
        return viewModelScope.launch { dao.insertNote(note) }
    }  // корутины

    fun getAllItemsFromList(listId:Int):LiveData<List<ShopingListItem>>{
       return dao.allShoppingListItems(listId).asLiveData()
    }

    fun insertShopListItem(shopListItem: ShopingListItem): Job {
        return viewModelScope.launch { dao.insertShopListItem(shopListItem) }
    }  // корутины

    fun insertShoppingListNames(name: ShopListNameItem): Job {
        return viewModelScope.launch { dao.insertShopListName(name) }
    }  // корутины

    fun updateNote(note: NoteItem): Job {
        return viewModelScope.launch { dao.updateNote(note) }
    }  // корутины

    fun updateShopListItem(shopListItem: ShopingListItem): Job {
        return viewModelScope.launch { dao.updateListItem(shopListItem) }
    }  // корутины

    fun updateShopListName(shopListName: ShopListNameItem): Job {
        return viewModelScope.launch { dao.updateShopListName(shopListName) }
    }  // корутины

    fun deleteNote(id: Int): Job {
        return viewModelScope.launch { dao.deleteNote(id) }
    }  // корутины

    fun deleteShopListName(id: Int): Job {
        return viewModelScope.launch { dao.deleteShopListName(id) }
    }  // корутины
}