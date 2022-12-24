package com.batuy.neco_shopinglist.activities

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.batuy.neco_shopinglist.db.MainDataBase

class MainApp:Application() {
    val dataBase by lazy { MainDataBase.getDataBase(this) }
}