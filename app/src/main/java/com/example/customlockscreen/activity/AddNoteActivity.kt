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
const val RESULT_CODE = 0


class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddNoteBinding

    private val END_TIME_TAG = "AddNoteActivity-endTime"
    private val ADD_NOTE_TIME_TAG = "AddNoteActivity-addNoteTime"

    private var isFirst = true

    @SuppressLint("SimpleDateFormat")
    private val format = SimpleDateFormat("yyyy-MM-dd-EE")

    private val today = format.format(MaterialDatePicker.todayInUtcMilliseconds())

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.noteAttributeLayout.chooseSortTv.text =data?.getStringExtra(SORT_NOTE_TEXT)
        super.onActivityResult(requestCode, resultCode, data)
    }

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)


        val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()


        binding.noteAttributeLayout.endTimeDate.visibility = View.GONE

        setSupportActionBar(binding.addNoteToolbar)

        binding.addNoteToolbar.setNavigationIcon(R.mipmap.back)

        binding.addNoteToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.noteAttributeLayout.endTimeSwitch.setOnClickListener {

            if(isFirst){
                binding.noteAttributeLayout.endTimeDate.visibility = View.VISIBLE
                isFirst = false
            }else{
                binding.noteAttributeLayout.endTimeDate.visibility = View.GONE
                isFirst = true
            }

        }

        // TODO: 2021/4/19 获取选择的时间 
        binding.noteAttributeLayout.endTimeDate.setOnClickListener {
           datePicker.show(this.supportFragmentManager,END_TIME_TAG)
            datePicker.addOnPositiveButtonClickListener {
                val chooseDay = format.format(it)
                binding.noteAttributeLayout.endTimeDate.text = chooseDay
                if(chooseDay<today){
                    binding.noteAttributeLayout.endTimeDate.setTextColor(ContextCompat.getColor(this,R.color.color_passed))
                }else{
                    binding.noteAttributeLayout.endTimeDate.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                }
            }
        }

        binding.noteAttributeLayout.addNoteDate.setOnClickListener {
            datePicker.show(this.supportFragmentManager,ADD_NOTE_TIME_TAG)
            datePicker.addOnPositiveButtonClickListener {
                val chooseDay = format.format(it)
                binding.noteAttributeLayout.addNoteDate.text = chooseDay
                if(chooseDay<today){
                    binding.noteAttributeLayout.addNoteDate.setTextColor(ContextCompat.getColor(this,R.color.color_passed))
                }else{
                    binding.noteAttributeLayout.addNoteDate.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
                }


            }
        }


        binding.noteAttributeLayout.chooseSortNoteLayout.setOnClickListener {
            val intent = Intent(this, SortNoteActivity::class.java)
            startActivityForResult(intent, RESULT_CODE)
        }



        setContentView(binding.root)
    }
}