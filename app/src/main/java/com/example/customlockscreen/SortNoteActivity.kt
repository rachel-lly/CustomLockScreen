package com.example.customlockscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customlockscreen.databinding.ActivitySortNoteBinding

class SortNoteActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySortNoteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySortNoteBinding.inflate(layoutInflater)

        binding.sortNoteToolbar.setNavigationIcon(R.mipmap.back)
        binding.sortNoteToolbar.setNavigationOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }
}