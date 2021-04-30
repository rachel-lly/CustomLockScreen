package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customlockscreen.R
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


        setContentView(binding.root)
    }
}