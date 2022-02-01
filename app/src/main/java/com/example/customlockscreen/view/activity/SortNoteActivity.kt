package com.example.customlockscreen.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.adapter.SortNoteAdapter
import com.example.customlockscreen.databinding.ActivitySortNoteBinding
import com.example.customlockscreen.model.bean.SortNote
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.viewmodel.DataViewModel
import com.example.customlockscreen.util.Code

class SortNoteActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySortNoteBinding

    private lateinit var adapter : SortNoteAdapter

    private lateinit var list:List<SortNote>

    private lateinit var onClickListener: SortNoteAdapter.ClickListener

    private val sortNoteDao = DataBase.dataBase.sortNoteDao()

    private lateinit var dataViewModel : DataViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  DataBindingUtil.setContentView(this,R.layout.activity_sort_note)

        binding.lifecycleOwner = this

        binding.sortNoteToolbar.setNavigationIcon(R.mipmap.back)
        binding.sortNoteToolbar.setNavigationOnClickListener {
            finish()
        }

        list = sortNoteDao.getAllSortNotes()

        onClickListener =object: SortNoteAdapter.ClickListener{
            override fun onClick(sortNoteName: String) {
                val intent = Intent()
                intent.putExtra(Code.SORT_NOTE_TEXT,sortNoteName)
                setResult(Code.RESULT_CODE,intent)
                finish()
            }

        }

        adapter = SortNoteAdapter(this,list,onClickListener)
        binding.sortNoteRecycleview.adapter = adapter
        binding.sortNoteRecycleview.layoutManager = GridLayoutManager(this,1)


        dataViewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        dataViewModel.getAllSortNotesByObserve().observe(this,{
            adapter.sortNoteList = it
            adapter.notifyDataSetChanged()
        })


        binding.addNoteSure.setOnClickListener {
            val intent = Intent(this,AddSortNoteActivity::class.java)
            startActivity(intent)
        }


    }
}