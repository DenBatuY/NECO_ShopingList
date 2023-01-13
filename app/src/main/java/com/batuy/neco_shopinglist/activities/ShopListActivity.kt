package com.batuy.neco_shopinglist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.batuy.neco_shopinglist.entities.LibraryItem
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
    private lateinit var textWatcher: TextWatcher
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
        textWatcher = textWatcher()
        return true
    }

    private fun textWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mainViewModel.getAllLibraryItems("%$s%")
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_item -> addNewShopItem(edItem?.text.toString())
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

    private fun addNewShopItem(name: String) {
        if (name.isEmpty()) return
        else {
            val item =
                ShopingListItem(
                    null,
                    name,
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

    private fun libraryItemObserver() {
        mainViewModel.libraryItems.observe(this) {
            val tempShopList = ArrayList<ShopingListItem>()
            it.forEach { item ->
                val shopItem = ShopingListItem(
                    item.id,
                    item.name,
                    "",
                    false,
                    0,
                    1
                )//для другой разметки в адаптере "itemType"=1!!
                tempShopList.add(shopItem)
            }
            adapter?.submitList(tempShopList)

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
                edItem?.addTextChangedListener(textWatcher)
                libraryItemObserver()
                mainViewModel.getAllItemsFromList(shoppingListNameItem?.id!!)
                    .removeObservers(this@ShopListActivity)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textWatcher)
                invalidateOptionsMenu()
                mainViewModel.libraryItems.removeObservers(this@ShopListActivity)
                edItem?.setText("")
                listItemObserver()
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
            ShopListItemAdapter.EDIT_LIBRARY -> editLibraryItem(shopListItem)
            ShopListItemAdapter.DELETE_LIBRARY -> {
                mainViewModel.deleteLibraryItem(shopListItem.id!!)
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
            }
            ShopListItemAdapter.ADD_LIBRARY_ITEM ->addNewShopItem(shopListItem.name)
        }

    }

    private fun editListItem(item: ShopingListItem) {
        EditListItemDialog.showDialog(this, item, object : EditListItemDialog.Listener {
            override fun onClick(item: ShopingListItem) {
                mainViewModel.updateShopListItem(item)
            }
        })
    }

    private fun editLibraryItem(item: ShopingListItem) {
        EditListItemDialog.showDialog(this, item, object : EditListItemDialog.Listener {
            override fun onClick(item: ShopingListItem) {
                mainViewModel.updateLibraryItem(LibraryItem(item.id, item.name))
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
            }
        })
    }

    private fun saveItemCount(){
        var checkedCounter=0
        adapter?.currentList?.forEach {
            if (it.itemChecked)checkedCounter++
        }
        val tempShopListNameItem=shoppingListNameItem?.copy(
            allItemCounter = adapter?.itemCount!!,
            checkedItemCounter = checkedCounter
        )
        mainViewModel.updateShopListName(tempShopListNameItem!!)
    }

    override fun onBackPressed() {
        saveItemCount()
        super.onBackPressed()
    }
}