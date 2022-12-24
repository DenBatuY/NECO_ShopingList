package com.batuy.neco_shopinglist.db

import androidx.room.Query
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.batuy.neco_shopinglist.entities.NoteItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("SELECT * FROM note_list")
    fun getAllItem(): Flow<List<NoteItem>>

    @Insert
    suspend fun insertNote(note: NoteItem)

    @Query("DELETE  FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Update
    suspend fun updateNote(note:NoteItem)

}