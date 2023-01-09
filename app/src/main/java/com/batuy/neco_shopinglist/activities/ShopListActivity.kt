package com.batuy.neco_shopinglist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.core.view.MenuItemCompat.expandActionView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.batuy.neco_shopinglist.R
import com.batuy.neco_shopinglist.databinding.ActivityShopListBinding
import com.batuy.neco_shopinglist.db.ShopListItemAdapter
import com.batuy.neco_shopinglist.db.TrainingViewModel
import com.batuy.neco_shopinglist.dialogs.EditListItemDialog
import com.batuy.neco_shopinglist.entities.ShopListNameItem
import com.batuy.neco_shopinglist.entities.ShopingListItem
import com.batuy.neco_shopinglist.utils.ShareHelper

class ShopListActivity : AppCompatActivity(), ShopListItemAdapter.Listener {
    //        private val mainViewModel: MainViewModel by viewModels() {
//        MainViewModel.MainViewModelFactory((applicationContext as MainApp).dataBase)
//    }
    lateinit var mainViewModel: TrainingViewModel
    private var shoppingListNameItem: ShopListNameItem? = null
    lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ShopListItemAdapter? = null
    lateinit var binding: ActivityShopListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this).get(TrainingViewModel::class.java)
        init()
        initRcView()
        listItemObserver()
    }

    private fun initRcView() = with(binding) {
        adapter = ShopListItemAdapter(this@ShopListActivity)
        rcView.layoutManager = LinearLayoutManager(this@ShopListActivity)
        rcView.adapter = adapter


    }


    private fun init() {
        shoppingListNameItem = intent.getSerializableExtra(SHOP_LIST_NAME) as ShopListNameItem
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shop_list_menu, menu)
        saveItem = menu?.findItem(R.id.save_item)!!
        val newItem = menu.findItem(R.id.new_item)
        edItem = newItem.actionView.findViewById(R.id.edNewShopItem) as EditText
        newItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_item -> addNewShopItem()
            R.id.delete_list -> {
                mainViewModel.deleteShopListName(shoppingListNameItem?.id!!, true)
                finish()
            }
            R.id.clear_list -> {
                mainViewModel.deleteShopListName(shoppingListNameItem?.id!!, false)
            }
            R.id.share_list -> {
                startActivity(
                    Intent.createChooser(
                        ShareHelper.shareShopList(
                            adapter?.currentList!!,
                            shoppingListNameItem?.name!!
                        ), "Shared by"
                    )
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewShopItem() {
        if (edItem?.text.toString().isEmpty()) return
        else {
            val item =
                ShopingListItem(
                    null,
                    edItem?.text.toString(),
                    null,
                    false,
                    shoppingListNameItem?.id!!,
                    0
                )
            mainViewModel.insertShopListItem(item)
            edItem?.setText("")
        }
    }

    private fun listItemObserver() {
        mainViewModel.getAllItemsFromList(shoppingListNameItem?.id!!).observe(this) {
            adapter?.submitList(it)
            binding.tvEmpty.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun expandActionView(): MenuItem.OnActionExpandListener {
        return object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                saveItem.isVisible = true
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                saveItem.isVisible = false
                invalidateOptionsMenu()
                return true
            }
        }
    }

    companion object {
        const val SHOP_LIST_NAME = "shop_list_name"
    }

    override fun onClickItem(shopListItem: ShopingListItem, state: Int) {
        when (state) {
            ShopListItemAdapter.CHECK_BOX -> mainViewModel.updateShopListItem(shopListItem)
            ShopListItemAdapter.EDIT -> editListItem(shopListItem)
        }

    }

    private fun editListItem(item: ShopingListItem) {
        EditListItemDialog.showDialog(this, item, object : EditListItemDialog.Listener {
            override fun onClick(item: ShopingListItem) {
                mainViewModel.updateShopListItem(item)
            }
        })
    }
}