package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.adapter.IconListAdapter
import com.example.customlockscreen.databinding.ActivityAddSortNoteBinding

class AddSortNoteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddSortNoteBinding

    private lateinit var onClickListener: IconListAdapter.ClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddSortNoteBinding.inflate(layoutInflater)

        binding.addSortNoteToolbar.setNavigationIcon(R.mipmap.back)
        binding.addSortNoteToolbar.setNavigationOnClickListener {
            finish()
        }

        onClickListener = object :IconListAdapter.ClickListener{
            override fun onClick(iconName: String) {
                // TODO: 2021/5/15 获取点击得到的iconName
            }
        }

        binding.addSortNoteCard.recycleView.adapter = IconListAdapter(this,onClickListener)
        binding.addSortNoteCard.recycleView.layoutManager = GridLayoutManager(this,6)


        setContentView(binding.root)
    }
}