package com.batuy.neco_shopinglist.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.batuy.neco_shopinglist.R
import com.batuy.neco_shopinglist.databinding.ActivityMainBinding
import com.batuy.neco_shopinglist.dialogs.NewListDialog
import com.batuy.neco_shopinglist.fragments.FragmentManager
import com.batuy.neco_shopinglist.fragments.NoteFragment
import com.batuy.neco_shopinglist.fragments.ShopListNamesFragment
import com.batuy.neco_shopinglist.settings.SettingsActivity

class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding
    private var currentTheme=""
    private var currentMenuItemId = R.id.notes
    private lateinit var defPref:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {

        defPref=PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme=defPref.getString("theme_key","blue").toString()
        setTheme(getSelectedTheme())

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        setBottomNavListener()
    }

    private fun setBottomNavListener() {
        binding.btNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.notes -> {
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.shop_list -> {
                    currentMenuItemId= R.id.shop_list
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                }
                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    override fun onClick(name: String) {

    }

    override fun onResume() {
        super.onResume()
        binding.btNav.selectedItemId = currentMenuItemId
        if (defPref.getString("theme_key","blue")!=currentTheme) recreate()
    }
    private fun getSelectedTheme():Int{
        return if (defPref.getString("theme_key","blue")=="blue"){
            R.style.Theme_NECO_ShoppingList
        }else{R.style.Theme_NECO_ShoppingListGreen}
    }
}