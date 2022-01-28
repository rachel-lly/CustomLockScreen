package com.example.customlockscreen.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.adapter.IconListAdapter
import com.example.customlockscreen.databinding.ActivityAddSortNoteBinding
import com.example.customlockscreen.model.bean.SortNote
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.util.Code
import com.example.customlockscreen.util.ToastUtil.Companion.toast

class AddSortNoteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddSortNoteBinding

    private lateinit var clickListener: IconListAdapter.ClickListener

    private val sortNoteDao = DataBase.dataBase.sortNoteDao()

    private lateinit var adapter :IconListAdapter

    private var mPosition:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityAddSortNoteBinding.inflate(layoutInflater)

        binding.addSortNoteToolbar.setNavigationIcon(R.mipmap.back)
        binding.addSortNoteToolbar.setNavigationOnClickListener {
            finish()
        }


        clickListener = object :IconListAdapter.ClickListener{
            override fun onClick(position: Int) {
                mPosition = position
            }
        }



        adapter = IconListAdapter(this,clickListener,0)
        val layoutManager = GridLayoutManager(this,6)


        binding.addSortNoteCard.recycleView.adapter = adapter
        binding.addSortNoteCard.recycleView.layoutManager = layoutManager


        binding.addNoteSure.setOnClickListener {
            saveSortNote()
        }

        setContentView(binding.root)
    }


    private fun saveSortNote() {

        val iconName = resources.getResourceEntryName(Code.iconList[mPosition])
        if(binding.addSortNoteCard.addSortNoteEt.text.isEmpty()){
            this.toast("分类本文字不能为空")
        }else{

            val sortNoteName = binding.addSortNoteCard.addSortNoteEt.text.toString()

            val nameList = sortNoteDao.getAllSortNotesName()

            if(nameList.contains(sortNoteName)){
                this.toast("该分类本已存在")
            }else{
                sortNoteDao.insertSortNote(SortNote(sortNoteName,iconName))
                this.toast("保存数据成功")
                finish()
            }
        }

    }
}