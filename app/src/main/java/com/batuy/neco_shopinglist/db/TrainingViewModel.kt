package com.batuy.neco_shopinglist.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.batuy.neco_shopinglist.entities.NoteItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TrainingViewModel(application:Application):AndroidViewModel(application) {
    val database = MainDataBase.getDataBase(application)
    val dao =database.getDao()


    val allNotes: LiveData<List<NoteItem>> =dao.getAllItem().asLiveData()

    fun insertNote(note: NoteItem): Job {
        return viewModelScope.launch { dao.insertNote(note) }
    }  // корутины

    fun updateNote(note: NoteItem): Job {
        return viewModelScope.launch { dao.updateNote(note) }
    }  // корутины

    fun deleteNote(id:Int): Job {
        return viewModelScope.launch { dao.deleteNote(id) }
    }  // корутины
}