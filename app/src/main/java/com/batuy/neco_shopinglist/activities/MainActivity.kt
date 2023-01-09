package com.batuy.neco_shopinglist.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.batuy.neco_shopinglist.R
import com.batuy.neco_shopinglist.databinding.ActivityMainBinding
import com.batuy.neco_shopinglist.dialogs.NewListDialog
import com.batuy.neco_shopinglist.fragments.BaseFragment
import com.batuy.neco_shopinglist.fragments.FragmentManager
import com.batuy.neco_shopinglist.fragments.NoteFragment
import com.batuy.neco_shopinglist.fragments.ShopListNamesFragment

class MainActivity : AppCompatActivity(),NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(),this)
        setBottomNavListener()
    }

    private fun setBottomNavListener(){
        binding.btNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.settings->{Toast.makeText(this@MainActivity,"Settings",Toast.LENGTH_SHORT).show()}
                R.id.notes->{FragmentManager.setFragment(NoteFragment.newInstance(),this)}
                R.id.shop_list->{FragmentManager.setFragment(ShopListNamesFragment.newInstance(),this)}
                R.id.new_item->{ FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    override fun onClick(name: String) {
        Log.d("test","$name")
    }
}