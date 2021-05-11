package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.ActivityEditNoteAttributeBinding

class EditNoteAttributeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityEditNoteAttributeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditNoteAttributeBinding.inflate(layoutInflater)

        binding.editNoteAttributeToolbar.setNavigationIcon(R.mipmap.back)
        binding.editNoteAttributeToolbar.setNavigationOnClickListener {
            finish()
        }


        setContentView(binding.root)
    }
}