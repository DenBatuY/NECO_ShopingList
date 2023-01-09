package com.batuy.neco_shopinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.batuy.neco_shopinglist.activities.ShopListActivity
import com.batuy.neco_shopinglist.databinding.FragmentShopListNamesBinding
import com.batuy.neco_shopinglist.db.ShopListNameAdapter
import com.batuy.neco_shopinglist.db.TrainingViewModel
import com.batuy.neco_shopinglist.dialogs.DeleteDialog
import com.batuy.neco_shopinglist.dialogs.NewListDialog
import com.batuy.neco_shopinglist.entities.ShopListNameItem
import com.batuy.neco_shopinglist.utils.TimeManager

class ShopListNamesFragment : BaseFragment(),ShopListNameAdapter.Listener {
    private lateinit var binding: FragmentShopListNamesBinding
    private lateinit var mainViewModel: TrainingViewModel
    private lateinit var adapter: ShopListNameAdapter

//    private val mainViewModel: MainViewModel by activityViewModels() {
//        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).dataBase)
//    }

    override fun onClickNew() {
      NewListDialog.showDialog(activity as AppCompatActivity,object :NewListDialog.Listener{
          override fun onClick(name: String) {
              val shopListName=ShopListNameItem(null,name,TimeManager.getCurrentTime(),
                  0,0,"")
              mainViewModel.insertShoppingListNames(shopListName)
          }
      },"")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(TrainingViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNamesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcView.layoutManager=LinearLayoutManager(activity)
        adapter= ShopListNameAdapter(this@ShopListNamesFragment)
        binding.rcView.adapter=adapter
        observer()
    }

    private fun observer() {
        mainViewModel.allShoppingListNames.observe(viewLifecycleOwner) {adapter.submitList(it)}
    }

    companion object {
        const val NEW_NOTE_KEY = "newNoteKey"
        const val EDIT_STATE_KEY = "newUpdate"

        fun newInstance() = ShopListNamesFragment()
    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity,object :DeleteDialog.Listener{
            override fun onClick() {
                mainViewModel.deleteShopListName(id,true)
            }
        })
    }

    override fun editAtem(shopListName: ShopListNameItem) {
        NewListDialog.showDialog(activity as AppCompatActivity,object :NewListDialog.Listener{
            override fun onClick(name: String) {

                mainViewModel.updateShopListName(shopListName.copy(name = name))
            }
        },shopListName.name)
    }

    override fun onClickItem(shopListName: ShopListNameItem) {
        val i = Intent(activity,ShopListActivity::class.java)
        i.putExtra(ShopListActivity.SHOP_LIST_NAME,shopListName)
        startActivity(i)
    }

}