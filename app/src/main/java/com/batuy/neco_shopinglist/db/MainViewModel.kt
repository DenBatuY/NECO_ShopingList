package com.batuy.neco_shopinglist.db

import androidx.lifecycle.*
import com.batuy.neco_shopinglist.entities.NoteItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(database:MainDataBase):ViewModel() {
    private val dao = database.getDao()

    val allNotes:LiveData<List<NoteItem>> =dao.getAllItem().asLiveData()

    fun insertNote(note:NoteItem):Job {
       return viewModelScope.launch { dao.insertNote(note) }
    }  // корутины

    fun updateNote(note:NoteItem):Job {
        return viewModelScope.launch { dao.updateNote(note) }
    }  // корутины

    fun deleteNote(id:Int):Job {
        return viewModelScope.launch { dao.deleteNote(id) }
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