package com.example.customlockscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customlockscreen.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)

        setSupportActionBar(binding.addNoteToolbar)

        binding.addNoteToolbar.setNavigationIcon(R.mipmap.back)

        binding.addNoteToolbar.setNavigationOnClickListener {
            finish()
        }



        setContentView(binding.root)
    }
}