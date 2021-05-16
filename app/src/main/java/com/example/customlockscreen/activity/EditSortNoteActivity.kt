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

    private lateinit var adapter: IconListAdapter


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

            val iconList = adapter.iconList

                if(mPosition!=-1){

                    val iconName = resources.getResourceEntryName(iconList[mPosition])
                    if(binding.editSortNoteCard.addSortNoteEt.text.isEmpty()){
                        Toast.makeText(this,"分类本文字不能为空",Toast.LENGTH_SHORT).show()
                    }else{

                        // TODO: 2021/5/16 更新数据 
                        val sortNoteName = binding.editSortNoteCard.addSortNoteEt.text.toString()
                        sortNoteDao.updateSortNote(SortNote(sortNoteName,iconName))
                        Toast.makeText(this,"保存数据成功--$sortNoteName:$iconName",Toast.LENGTH_SHORT).show()
                    }


                }else{
                    Toast.makeText(this,"请选择一个图标",Toast.LENGTH_SHORT).show()
                }





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


                val iconList = adapter.iconList

                if(mPosition!=-1){
                    // TODO: 2021/5/15 保存信息
                    val iconName = resources.getResourceEntryName(iconList[mPosition])
                    Toast.makeText(this,"$iconName",Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(this,"请选择一个图标",Toast.LENGTH_SHORT).show()
                }

            }
        }

        return true
    }
}