package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.adapter.IconListAdapter
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




        val adapter = IconListAdapter(this)


        binding.editSortNoteCard.recycleView.adapter = adapter
        binding.editSortNoteCard.recycleView.layoutManager = GridLayoutManager(this,6)

        binding.editNoteSure.setOnClickListener {
            val checkBoxMap = adapter.positionMap
            val iconList = adapter.iconList



                var chooseCount = 0
                var choosePosition = -1

                for(i in 0..checkBoxMap.size-1){
                    if(checkBoxMap.get(i)==true){
                        chooseCount++
                        choosePosition = i
                    }
                }

                if(chooseCount==1){
                    // TODO: 2021/5/15 保存信息
                    val iconName = resources.getResourceEntryName(iconList[choosePosition])


                }else{
                    Toast.makeText(this,"请选择一个图标",Toast.LENGTH_SHORT).show()
                }





        }

        setContentView(binding.root)
    }
}