package com.batuy.neco_shopinglist.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.batuy.neco_shopinglist.activities.MainApp
import com.batuy.neco_shopinglist.activities.NewNoteActivity
import com.batuy.neco_shopinglist.databinding.FragmentNoteBinding
import com.batuy.neco_shopinglist.db.MainViewModel
import com.batuy.neco_shopinglist.db.NoteAdapter
import com.batuy.neco_shopinglist.db.TrainingViewModel
import com.batuy.neco_shopinglist.entities.NoteItem


class NoteFragment : BaseFragment(), NoteAdapter.Listener {
    private lateinit var binding: FragmentNoteBinding
    private lateinit var adapter: NoteAdapter
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var defPref: SharedPreferences

    private lateinit var mainViewModel: TrainingViewModel


//    private val mainViewModel: MainViewModel by activityViewModels() {
//        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).dataBase)
//    }

    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // mainViewModel.allNotes.observe(this,{it})
        mainViewModel = ViewModelProvider(this).get(TrainingViewModel::class.java)
        onEditResult()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        defPref=PreferenceManager.getDefaultSharedPreferences(requireActivity())
        binding.rcView.layoutManager = getLayoutManager()
        adapter = NoteAdapter(this@NoteFragment,defPref)
        binding.rcView.adapter = adapter

        observer()
    }

    private fun getLayoutManager():RecyclerView.LayoutManager{
        return when(defPref.getString("note_style_key","Linear")){
            "Linear"->{LinearLayoutManager(activity)}
            else -> {GridLayoutManager(activity,2)}
        }
    }

    private fun observer() {

        mainViewModel.allNotes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }


    companion object {
        const val NEW_NOTE_KEY = "newNoteKey"
        const val EDIT_STATE_KEY = "newUpdate"

        fun newInstance() = NoteFragment()
    }

    private fun onEditResult() {
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                // Log.d("test", "intent: ${it.data?.getStringExtra(NEW_NOTE_KEY)}")
                val editState = it.data?.getStringExtra(EDIT_STATE_KEY)
                if (editState == "update") {
                    mainViewModel.updateNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
                } else {
                    mainViewModel.insertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
                }

            }
        }
    }

    override fun deleteItem(id: Int) {
        mainViewModel.deleteNote(id)
    }

    override fun onClickItem(note: NoteItem) {
        val intent = Intent(activity, NewNoteActivity::class.java)
        intent.putExtra(NEW_NOTE_KEY, note)
        editLauncher.launch(intent)
    }
}