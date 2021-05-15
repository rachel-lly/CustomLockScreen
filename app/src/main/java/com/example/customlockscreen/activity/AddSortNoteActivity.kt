package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.adapter.IconListAdapter
import com.example.customlockscreen.databinding.ActivityAddSortNoteBinding

class AddSortNoteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddSortNoteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddSortNoteBinding.inflate(layoutInflater)

        binding.addSortNoteToolbar.setNavigationIcon(R.mipmap.back)
        binding.addSortNoteToolbar.setNavigationOnClickListener {
            finish()
        }



        binding.addSortNoteCard.recycleView.adapter = IconListAdapter(this)
        binding.addSortNoteCard.recycleView.layoutManager = GridLayoutManager(this,6)

        val adapter = IconListAdapter(this)


        binding.addSortNoteCard.recycleView.adapter = adapter
        binding.addSortNoteCard.recycleView.layoutManager = GridLayoutManager(this,6)

        binding.addNoteSure.setOnClickListener {
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
                Toast.makeText(this,"请选择一个图标", Toast.LENGTH_SHORT).show()
            }





        }


        setContentView(binding.root)
    }
}