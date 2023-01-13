package com.batuy.neco_shopinglist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.batuy.neco_shopinglist.R
import com.batuy.neco_shopinglist.databinding.ActivityNewNoteBinding
import com.batuy.neco_shopinglist.entities.NoteItem
import com.batuy.neco_shopinglist.fragments.NoteFragment
import com.batuy.neco_shopinglist.utils.HTML_Manager
import com.batuy.neco_shopinglist.utils.MyOnTouchListener
import com.batuy.neco_shopinglist.utils.TimeManager
import java.util.*

class NewNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewNoteBinding

    private var note: NoteItem? = null
    private var pref:SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref=PreferenceManager.getDefaultSharedPreferences(this)
        actionbarHome()
        getNote()
        init()
        onClickColor()
        setTextSize()

    }

    private fun onClickColor()= with(binding){
        imageButtonRed.setOnClickListener {  setColorForSelectedText(R.color.picker_red)}
        imageButtonGreen.setOnClickListener { setColorForSelectedText(R.color.picker_green) }
        imageButtonBlue.setOnClickListener { setColorForSelectedText(R.color.picker_blue) }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(){
        binding.TableLayoutColors.setOnTouchListener(MyOnTouchListener())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }//Добавляем кнопку Save

    private fun getNote() {
        val sNote = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY)
        if (sNote != null) {
            note = sNote as NoteItem
            binding.edTitle.setText(note?.title)
            binding.edDescription.setText(HTML_Manager.getFromHtml(note?.content!!))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_save) {
            setMainActResult()

        } else if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.id_bold) {
            setBoldForSelectedText()
        } else if (item.itemId == R.id.id_color) {
            if (binding.TableLayoutColors.isShown) {
                closeColorPicker()
            } else {
                openColorPicker()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBoldForSelectedText() {
        val startPosition = binding.edDescription.selectionStart
        val endPosition = binding.edDescription.selectionEnd
        val styles =
            binding.edDescription.text.getSpans(startPosition, endPosition, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if (styles.isNotEmpty()) {
            binding.edDescription.text.removeSpan(styles[0])// убираем стили с выбраного текста
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }

        binding.edDescription.text.setSpan(
            boldStyle,
            startPosition,
            endPosition,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.edDescription.text.trim()
        binding.edDescription.setSelection(startPosition)
    }


    private fun setColorForSelectedText(colorId:Int) {
        val startPosition = binding.edDescription.selectionStart
        val endPosition = binding.edDescription.selectionEnd
        val styles =
            binding.edDescription.text.getSpans(startPosition, endPosition, ForegroundColorSpan::class.java)
        if (styles.isNotEmpty()) {
            binding.edDescription.text.removeSpan(styles[0])// убираем стили с выбраного текста
        }

        binding.edDescription.text.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this@NewNoteActivity,colorId)),
            startPosition,
            endPosition,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.edDescription.text.trim()
        binding.edDescription.setSelection(startPosition)
    }

    private fun actionbarHome() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Показать кнопку Home


    }

    private fun setMainActResult() {
        val tempNote: NoteItem?
        val i = Intent()
        var editState = "new"
        if (note == null) tempNote = createNewNote()
        else {
            tempNote = updateNote()
            editState = "update"
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
            HTML_Manager.toHtml(binding.edDescription.text),
           TimeManager.getCurrentTime(),
            ""
        )
    }

    private fun updateNote(): NoteItem? {
        return note?.copy(
            title = binding.edTitle.text.toString(),
            content = HTML_Manager.toHtml(binding.edDescription.text)
        )
    }



    private fun openColorPicker() {
        binding.TableLayoutColors.visibility = View.VISIBLE
        val openAnimation = AnimationUtils.loadAnimation(this, R.anim.open_color_picker)
        binding.TableLayoutColors.startAnimation(openAnimation)
    }

    private fun closeColorPicker() {
        val openAnimation = AnimationUtils.loadAnimation(this, R.anim.close_color_picker)
        openAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.TableLayoutColors.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
        binding.TableLayoutColors.startAnimation(openAnimation)
    }

    private fun setTextSize()= with(binding){
        edTitle.setTextSize(pref?.getString("title_size_key","16"))
        edDescription.setTextSize(pref?.getString("content_size_key","14"))
    }

    private fun EditText.setTextSize(size:String?){
        if (size!=null){this.textSize=size.toFloat()}
    }
}