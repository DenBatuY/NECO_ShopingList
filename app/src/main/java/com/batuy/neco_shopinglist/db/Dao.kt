package com.batuy.neco_shopinglist.db

import androidx.room.Query
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.batuy.neco_shopinglist.entities.LibraryItem
import com.batuy.neco_shopinglist.entities.NoteItem
import com.batuy.neco_shopinglist.entities.ShopListNameItem
import com.batuy.neco_shopinglist.entities.ShopingListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM note_list")
    fun getAllItem(): Flow<List<NoteItem>>

    @Query("SELECT * FROM shoping_listnames")
    fun allShoppingListNames(): Flow<List<ShopListNameItem>>

    @Query("SELECT * FROM shop_list_item WHERE listId LIKE :listId")
    fun allShoppingListItems(listId:Int): Flow<List<ShopingListItem>>

    @Query("SELECT * FROM library_item WHERE name LIKE :name")
    suspend fun allLibraryItems(name:String): List<LibraryItem>

    @Insert
    suspend fun insertLibraryItem(libraryItem: LibraryItem)

    @Insert
    suspend fun insertNote(note: NoteItem)

    @Insert
    suspend fun insertShopListItem(shopListItem: ShopingListItem)

    @Insert
    suspend fun insertShopListName(name: ShopListNameItem)

    @Query("DELETE  FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Query("DELETE FROM shop_list_item WHERE listId LIKE :listId")
    suspend fun deleteShopItemsByListId(listId:Int)

    @Query("DELETE  FROM shoping_listnames WHERE id IS :id")
    suspend fun deleteShopListName(id: Int)

    @Query("DELETE  FROM library_item WHERE id IS :id")
    suspend fun deleteLibraryItem(id: Int)

    @Update
    suspend fun updateNote(note:NoteItem)

    @Update
    suspend fun updateLibraryItem(item:LibraryItem)

    @Update
    suspend fun updateListItem(shopListItem:ShopingListItem)

    @Update
    suspend fun updateShopListName(shopListName:ShopListNameItem)

}