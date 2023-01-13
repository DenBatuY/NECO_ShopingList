package com.batuy.neco_shopinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.batuy.neco_shopinglist.R
import com.batuy.neco_shopinglist.databinding.EditListItemDialogBinding
import com.batuy.neco_shopinglist.databinding.NewListDialogBinding
import com.batuy.neco_shopinglist.entities.ShopingListItem

object EditListItemDialog {
    fun showDialog(context: Context,item: ShopingListItem, listener: Listener ) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = EditListItemDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.edNameDialog.setText(item.name)
        binding.edInfoDialog.setText(item.itemInfo)
        if (item.itemType==1) binding.edInfoDialog.visibility=View.GONE
        binding.bUpdateDialog.setOnClickListener {
            if (binding.edNameDialog.text.toString().isNotEmpty()){
                val tvInfo = if (binding.edInfoDialog.text.toString().isEmpty()){null}
                else binding.edInfoDialog.text.toString()
                listener.onClick(item.copy(name = binding.edNameDialog.text.toString(), itemInfo = tvInfo))
            }
                dialog?.dismiss()
        }

        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick(item: ShopingListItem)
    }
}