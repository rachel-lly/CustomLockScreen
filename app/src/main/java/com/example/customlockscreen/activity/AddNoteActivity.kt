package com.example.customlockscreen.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.ActivityAddNoteBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import kotlin.time.ExperimentalTime

const val SORT_NOTE_TEXT = "SORT_NOTE_TEXT"


class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddNoteBinding

    private val END_TIME_TAG = "AddNoteActivity-endTime"
    private val ADD_NOTE_TIME_TAG = "AddNoteActivity-addNoteTime"

    private var isFirst = true

    @SuppressLint("SimpleDateFormat")
    private val format = SimpleDateFormat("yyyy-MM-dd-EE")

    private val today = format.format(MaterialDatePicker.todayInUtcMilliseconds())

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)

        binding.chooseSortTv.text = intent?.getStringExtra(SORT_NOTE_TEXT)


        val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()


        binding.endTimeDate.visibility = View.GONE

        setSupportActionBar(binding.addNoteToolbar)

        binding.addNoteToolbar.setNavigationIcon(R.mipmap.back)

        binding.addNoteToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.endTimeSwitch.setOnClickListener {

            if(isFirst){
                binding.endTimeDate.visibility = View.VISIBLE
                isFirst = false
            }else{
                binding.endTimeDate.visibility = View.GONE
                isFirst = true
            }

        }

        // TODO: 2021/4/19 获取选择的时间 
        binding.endTimeDate.setOnClickListener {
           datePicker.show(this.supportFragmentManager,END_TIME_TAG)
            datePicker.addOnPositiveButtonClickListener {
                val chooseDay = format.format(it)
                binding.endTimeDate.text = chooseDay
                if(chooseDay<today){
                    binding.endTimeDate.setTextColor(ContextCompat.getColor(this,R.color.color_passed))
                }else{
                    binding.endTimeDate.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                }
            }
        }

        binding.addNoteDate.setOnClickListener {
            datePicker.show(this.supportFragmentManager,ADD_NOTE_TIME_TAG)
            datePicker.addOnPositiveButtonClickListener {
                val chooseDay = format.format(it)
                binding.addNoteDate.text = chooseDay
                if(chooseDay<today){
                    binding.addNoteDate.setTextColor(ContextCompat.getColor(this,R.color.color_passed))
                }else{
                    binding.addNoteDate.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
                }


            }
        }


        binding.chooseSortNoteLayout.setOnClickListener {
            val intent = Intent(this, SortNoteActivity::class.java)
            startActivity(intent)
        }



        setContentView(binding.root)
    }
}