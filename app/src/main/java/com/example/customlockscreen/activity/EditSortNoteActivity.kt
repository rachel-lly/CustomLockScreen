package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.adapter.IconListAdapter
import com.example.customlockscreen.databinding.ActivityEditSortNoteBinding
import com.example.customlockscreen.model.SortNote

const val SORT_NOTE = "SORT_NOTE"

class EditSortNoteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditSortNoteBinding

    private lateinit var onClickListener: IconListAdapter.ClickListener


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

        onClickListener = object :IconListAdapter.ClickListener{
            override fun onClick(iconName: String) {
                // TODO: 2021/5/15 获取点击得到的iconName
            }
        }

        binding.editSortNoteCard.recycleView.adapter = IconListAdapter(this,onClickListener)
        binding.editSortNoteCard.recycleView.layoutManager = GridLayoutManager(this,6)


        setContentView(binding.root)
    }
}