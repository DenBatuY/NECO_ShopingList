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
    val allShoppingListNames: LiveData<List<ShopListNameItem>> =
        dao.allShoppingListNames().asLiveData()

    fun insertNote(note: NoteItem): Job {
        return viewModelScope.launch { dao.insertNote(note) }
    }

    fun getAllItemsFromList(listId: Int): LiveData<List<ShopingListItem>> {
        return dao.allShoppingListItems(listId).asLiveData()
    }

    fun insertShopListItem(shopListItem: ShopingListItem): Job {
        return viewModelScope.launch { dao.insertShopListItem(shopListItem) }
    }

    fun insertShoppingListNames(name: ShopListNameItem): Job {
        return viewModelScope.launch { dao.insertShopListName(name) }
    }

    fun updateNote(note: NoteItem): Job {
        return viewModelScope.launch { dao.updateNote(note) }
    }

    fun updateShopListItem(shopListItem: ShopingListItem): Job {
        return viewModelScope.launch { dao.updateListItem(shopListItem) }
    }

    fun updateShopListName(shopListName: ShopListNameItem): Job {
        return viewModelScope.launch { dao.updateShopListName(shopListName) }
    }

    fun deleteNote(id: Int): Job {
        return viewModelScope.launch { dao.deleteNote(id) }
    }

    fun deleteShopListName(id: Int, deleteList:Boolean): Job {
        return viewModelScope.launch {
            if (deleteList)dao.deleteShopListName(id)
            dao.deleteShopItemsByListId(id)
        }
    }
}