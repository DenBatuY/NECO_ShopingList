package com.batuy.neco_shopinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shoping_listnames")

data class ShopListNameItem(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,

    @ColumnInfo(name = "name")
    val name:String,

    @ColumnInfo(name = "time")
    val time:String,

    @ColumnInfo(name = "allItemCounter")
    val allItemCounter:Int,

    @ColumnInfo(name = "checkedItemCounter")
    val checkedItemCounter:Int,

    @ColumnInfo(name = "itemsId")
    val itemsId:String
):java.io.Serializable
