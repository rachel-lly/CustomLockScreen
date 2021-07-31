package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.adapter.IconListAdapter
import com.example.customlockscreen.databinding.ActivityEditSortNoteBinding
import com.example.customlockscreen.model.bean.SortNote
import com.example.customlockscreen.model.db.DataBase

const val SORT_NOTE = "SORT_NOTE"

class EditSortNoteActivity : AppCompatActivity() {

    private var mPosition:Int = -1

    private lateinit var binding : ActivityEditSortNoteBinding

    private lateinit var clickListener: IconListAdapter.ClickListener

    private val sortNoteDao = DataBase.dataBase.sortNoteDao()

    private val labelDao = DataBase.dataBase.labelDao()

    private lateinit var adapter: IconListAdapter

    private var sortNote:SortNote? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditSortNoteBinding.inflate(layoutInflater)

        binding.editSortNoteToolbar.setNavigationIcon(R.mipmap.back)
        binding.editSortNoteToolbar.setNavigationOnClickListener {
            finish()
        }



        sortNote = intent?.getParcelableExtra(SORT_NOTE)


        if (sortNote != null) {
            binding.editSortNoteCard.addSortNoteEt.text = SpannableStringBuilder(sortNote!!.name)

        }


        clickListener = object :IconListAdapter.ClickListener{
            override fun onClick(position: Int) {
                mPosition = position
            }

        }


        adapter = IconListAdapter(this,clickListener)
        val layoutManager = GridLayoutManager(this,6)



        binding.editSortNoteCard.recycleView.adapter = adapter
        binding.editSortNoteCard.recycleView.layoutManager = layoutManager


        binding.editNoteSure.setOnClickListener {
            updateSortNote()
        }

        binding.editNoteDelete.setOnClickListener {

            if(sortNote!=null){

                if(labelDao.getSameSortNoteLabelList(sortNote!!.name).size!=0){
                    Toast.makeText(this,"该分类本下有事件，删除失败",Toast.LENGTH_SHORT).show()
                }else{
                    sortNoteDao.deleteSortNote(sortNote!!)
                    Toast.makeText(this,"删除分类本成功",Toast.LENGTH_SHORT).show()
                    finish()
                }


            }
        }

        setContentView(binding.root)
    }

    private fun updateSortNote() {
        val iconList = adapter.iconList

        if(mPosition!=-1){

            val iconName = resources.getResourceEntryName(iconList[mPosition])
            if(binding.editSortNoteCard.addSortNoteEt.text.isEmpty()){
                Toast.makeText(this,"分类本文字不能为空",Toast.LENGTH_SHORT).show()
            }else{
                val sortNoteName = binding.editSortNoteCard.addSortNoteEt.text.toString()

                if(sortNote!=null){

                    val addSortNote = SortNote(sortNoteName,iconName)

                    if(sortNote!!.name.equals(sortNoteName)){
                        sortNoteDao.updateSortNote(addSortNote)
                        Toast.makeText(this,"修改数据成功",Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        val nameList = sortNoteDao.getAllSortNotesName()

                        var flag = false

                        for(name in nameList){
                            if(name.equals(sortNoteName)){
                                flag = true
                                break
                            }
                        }

                        if(flag){
                            Toast.makeText(this,"该分类本已存在",Toast.LENGTH_SHORT).show()
                        }else{
                            sortNoteDao.deleteSortNote(sortNote!!)
                            sortNoteDao.insertSortNote(addSortNote)
                            Toast.makeText(this,"修改数据成功",Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                }

            }

        }else{
            Toast.makeText(this,"请选择一个图标",Toast.LENGTH_SHORT).show()
        }
    }
}