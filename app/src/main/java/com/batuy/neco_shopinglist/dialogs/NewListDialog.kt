package com.batuy.neco_shopinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.batuy.neco_shopinglist.R
import com.batuy.neco_shopinglist.databinding.NewListDialogBinding

object NewListDialog {
    fun showDialog(context: Context, listener: Listener, name: String) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = NewListDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.edNewListName.setText(name)
        if (name.isNotEmpty()){binding.btCreate.text=context.getString(R.string.update)}
        binding.btCreate.setOnClickListener {
            val listName = binding.edNewListName.text.toString()
            if (listName.isNotEmpty()) {
                listener.onClick(listName)
                dialog?.dismiss()
            }

        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick(name: String)
    }
}