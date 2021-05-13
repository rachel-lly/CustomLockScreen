package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.ActivityEditNoteAttributeBinding
import com.example.customlockscreen.databinding.ActivityEditSortNoteBinding
import com.example.customlockscreen.model.SortNote

const val SORT_NOTE = "SORT_NOTE"

class EditSortNoteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditSortNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditSortNoteBinding.inflate(layoutInflater)

        binding.editSortNoteToolbar.setNavigationIcon(R.mipmap.back)
        binding.editSortNoteToolbar.setNavigationOnClickListener {
            finish()
        }

        var sortNote = intent?.getParcelableExtra<SortNote>(SORT_NOTE)

        if (sortNote != null) {
            binding.editSortNoteCard.addSortNoteEt.text = SpannableStringBuilder(sortNote.name)

        }


        setContentView(binding.root)
    }
}