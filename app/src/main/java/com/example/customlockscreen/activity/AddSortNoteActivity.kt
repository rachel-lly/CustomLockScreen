package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.adapter.IconListAdapter
import com.example.customlockscreen.databinding.ActivityAddSortNoteBinding
import com.example.customlockscreen.model.bean.SortNote
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.model.db.SortNoteDao


class AddSortNoteActivity : AppCompatActivity() {


    private lateinit var binding:ActivityAddSortNoteBinding

    private lateinit var clickListener: IconListAdapter.ClickListener

    private val sortNoteDao = DataBase.dataBase.sortNoteDao()

    private var mPosition:Int = -1


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


        val adapter = IconListAdapter(this,clickListener)
        val layoutManager = GridLayoutManager(this,6)



        binding.addSortNoteCard.recycleView.adapter = adapter
        binding.addSortNoteCard.recycleView.layoutManager = layoutManager


        binding.addNoteSure.setOnClickListener {

            val iconList = adapter.iconList

            if(mPosition!=-1){

                // TODO: 2021/5/16 保存数据 
                val iconName = resources.getResourceEntryName(iconList[mPosition])
                if(binding.addSortNoteCard.addSortNoteEt.text.isEmpty()){
                    Toast.makeText(this,"分类本文字不能为空",Toast.LENGTH_SHORT).show()
                }else{
                    val sortNoteName = binding.addSortNoteCard.addSortNoteEt.text.toString()
                    sortNoteDao.insertSortNote(SortNote(sortNoteName,iconName))
                    Toast.makeText(this,"保存数据成功--$sortNoteName:$iconName",Toast.LENGTH_SHORT).show()
                }


            }else{
                Toast.makeText(this,"请选择一个图标",Toast.LENGTH_SHORT).show()
            }





        }

        setContentView(binding.root)
    }
}