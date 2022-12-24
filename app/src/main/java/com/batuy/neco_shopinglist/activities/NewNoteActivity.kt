package com.batuy.neco_shopinglist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.batuy.neco_shopinglist.R
import com.batuy.neco_shopinglist.databinding.ActivityNewNoteBinding
import com.batuy.neco_shopinglist.entities.NoteItem
import com.batuy.neco_shopinglist.fragments.NoteFragment
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewNoteBinding

    private var note: NoteItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionbarHome()
        getNote()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }//Добавляем кнопку Save

    private fun getNote() {
        val sNote = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY)
        if (sNote != null) {
            note=sNote as NoteItem
            binding.edTitle.setText(note?.title)
            binding.edDescription.setText(note?.content)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_save) {
             setMainActResult()

        } else if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun actionbarHome() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Показать кнопку Home


    }

    private fun setMainActResult() {
        val tempNote:NoteItem?
        val i = Intent()
        var editState= "new"
        if (note==null) tempNote=createNewNote()
        else {
            tempNote = updateNote()
            editState="update"
        }
        i.putExtra(NoteFragment.NEW_NOTE_KEY, tempNote)
        i.putExtra(NoteFragment.EDIT_STATE_KEY, editState)
        setResult(RESULT_OK, i)
        finish()
    }

    private fun createNewNote(): NoteItem {
        return NoteItem(
            null,
            binding.edTitle.text.toString(),
            binding.edDescription.text.toString(),
            getCurrentTime(),
            ""
        )
    }

    private fun updateNote():NoteItem? {
       return note?.copy(
            title = binding.edTitle.text.toString(),
            content = binding.edDescription.text.toString()
        )
    }

    private fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("hh:mm:ss-yyyy/MM/dd", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)

    }
}