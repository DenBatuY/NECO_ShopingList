package com.batuy.neco_shopinglist.fragments

import androidx.appcompat.app.AppCompatActivity
import com.batuy.neco_shopinglist.R

object FragmentManager {
    var currentFrag: BaseFragment? = null

    fun setFragment(newFragment: BaseFragment, activity: AppCompatActivity) {

        val transaction = activity.supportFragmentManager.beginTransaction()
            .replace(R.id.placeHolder, newFragment).commit()
        currentFrag=newFragment
    }
}