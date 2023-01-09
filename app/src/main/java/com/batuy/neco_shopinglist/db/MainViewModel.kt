package com.batuy.neco_shopinglist.db

import androidx.lifecycle.*
import com.batuy.neco_shopinglist.entities.NoteItem
import com.batuy.neco_shopinglist.entities.ShopListNameItem
import com.batuy.neco_shopinglist.entities.ShopingListItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(database:MainDataBase):ViewModel() {
    private val dao = database.getDao()

    val allNotes:LiveData<List<NoteItem>> =dao.getAllItem().asLiveData()
    val allShoppingListNames: LiveData<List<ShopListNameItem>> = dao.allShoppingListNames().asLiveData()

    fun getAllItemsFromList(listId:Int):LiveData<List<ShopingListItem>>{
        return dao.allShoppingListItems(listId).asLiveData()
    }

    fun insertNote(note:NoteItem):Job {
       return viewModelScope.launch { dao.insertNote(note) }
    }  // корутины

    fun insertShopListItem(shopListItem: ShopingListItem): Job {
        return viewModelScope.launch { dao.insertShopListItem(shopListItem) }
    }  // корутины

    fun updateNote(note:NoteItem):Job {
        return viewModelScope.launch { dao.updateNote(note) }
    }  // корутины

    fun updateShopListItem(shopListItem: ShopingListItem): Job {
        return viewModelScope.launch { dao.updateListItem(shopListItem) }
    }  // корутины

    fun updateShopListName(shopListName: ShopListNameItem): Job {
        return viewModelScope.launch { dao.updateShopListName(shopListName) }
    }  // корутины

    fun deleteNote(id:Int):Job {
        return viewModelScope.launch { dao.deleteNote(id) }
    }  // корутины

    fun insertShoppingListNames(name: ShopListNameItem): Job {
        return viewModelScope.launch { dao.insertShopListName(name) }
    }  // корутины

    fun deleteShopListName(id: Int): Job {
        return viewModelScope.launch { dao.deleteShopListName(id) }
    }  // корутины

    class MainViewModelFactory(private val database: MainDataBase):ViewModelProvider.Factory{
        // Не заморачиватся с этим класом, переписать как есть!!!
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw java.lang.IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}