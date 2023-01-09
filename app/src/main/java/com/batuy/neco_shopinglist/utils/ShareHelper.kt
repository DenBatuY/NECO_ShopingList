package com.batuy.neco_shopinglist.utils

import android.content.Intent
import com.batuy.neco_shopinglist.entities.ShopingListItem

object ShareHelper {

    fun shareShopList(shopList: List<ShopingListItem>, name: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/name"
        intent.putExtra(Intent.EXTRA_TEXT, makeShareText(shopList, name))
        return intent
    }

    private fun makeShareText(shopList: List<ShopingListItem>, name: String): String {
        val sBuilder = StringBuilder()
        sBuilder.append("<<$name>>")
        sBuilder.append("\n")
        var counter = 0
        shopList.forEach {
            sBuilder.append("${++counter}-${it.name}  (${it.itemInfo})")
            sBuilder.append("\n")
        }
        return sBuilder.toString()
    }
}