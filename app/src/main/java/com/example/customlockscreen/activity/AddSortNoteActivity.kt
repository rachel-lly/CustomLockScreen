package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.adapter.IconListAdapter
import com.example.customlockscreen.databinding.ActivityAddSortNoteBinding
import com.example.customlockscreen.model.bean.SortNote
import com.example.customlockscreen.model.db.DataBase

class AddSortNoteActivity : AppCompatActivity() {


    private lateinit var binding:ActivityAddSortNoteBinding

    private lateinit var clickListener: IconListAdapter.ClickListener

    private val sortNoteDao = DataBase.dataBase.sortNoteDao()

    private lateinit var adapter :IconListAdapter

    private var mPosition:Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityAddSortNoteBinding.inflate(layoutInflater)

        setSupportActionBar(binding.addSortNoteToolbar)

        binding.addSortNoteToolbar.setNavigationIcon(R.mipmap.back)
        binding.addSortNoteToolbar.setNavigationOnClickListener {
            finish()
        }


        clickListener = object :IconListAdapter.ClickListener{
            override fun onClick(position: Int) {
                mPosition = position
            }

        }


        adapter = IconListAdapter(this,clickListener)
        val layoutManager = GridLayoutManager(this,6)


        binding.addSortNoteCard.recycleView.adapter = adapter
        binding.addSortNoteCard.recycleView.layoutManager = layoutManager


        binding.addNoteSure.setOnClickListener {
            saveSortNote()
        }

        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_save_data_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when(item.itemId){

            R.id.save_data ->{
                saveSortNote()
            }

        }

        return true

    }

    private fun saveSortNote() {

        val iconList = adapter.iconList

        if(mPosition!=-1){

            val iconName = resources.getResourceEntryName(iconList[mPosition])
            if(binding.addSortNoteCard.addSortNoteEt.text.isEmpty()){
                Toast.makeText(this,"分类本文字不能为空",Toast.LENGTH_SHORT).show()
            }else{

                val sortNoteName = binding.addSortNoteCard.addSortNoteEt.text.toString()

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
                    sortNoteDao.insertSortNote(SortNote(sortNoteName,iconName))
                    Toast.makeText(this,"保存数据成功--$sortNoteName:$iconName",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        }else{
            Toast.makeText(this,"请选择一个图标",Toast.LENGTH_SHORT).show()
        }
    }
}