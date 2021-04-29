package com.example.customlockscreen.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.adapter.SortNoteAdapter
import com.example.customlockscreen.databinding.ActivitySortNoteBinding
import com.example.customlockscreen.model.SortNote

class SortNoteActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySortNoteBinding

    private var list = ArrayList<SortNote>()

    private lateinit var onClickListener: SortNoteAdapter.ClickListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySortNoteBinding.inflate(layoutInflater)

        binding.sortNoteToolbar.setNavigationIcon(R.mipmap.back)
        binding.sortNoteToolbar.setNavigationOnClickListener {
            finish()
        }

        list.add(SortNote("纪念日",resources.getResourceEntryName(R.mipmap.anniversary)))
        list.add(SortNote("工作",resources.getResourceEntryName(R.mipmap.work)))
        list.add(SortNote("生活",resources.getResourceEntryName(R.mipmap.life)))

        onClickListener =object: SortNoteAdapter.ClickListener{
            override fun onClick(sortNoteName: String) {
                var intent = Intent()
                intent.putExtra(SORT_NOTE_TEXT,sortNoteName)
                setResult(RESULT_CODE,intent)
                finish()
            }

        }

        var adapter = SortNoteAdapter(this,list,onClickListener)
        binding.sortNoteRecycleview.adapter = adapter
        binding.sortNoteRecycleview.layoutManager = GridLayoutManager(this,1)




        binding.addNoteSure.setOnClickListener {
            var intent = Intent(this,AddSortNoteActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }
}