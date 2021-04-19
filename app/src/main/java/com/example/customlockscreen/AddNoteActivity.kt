package com.example.customlockscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.customlockscreen.databinding.ActivityAddNoteBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import kotlin.time.ExperimentalTime

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddNoteBinding

    private val END_TIME_TAG = "AddNoteActivity-endTime"
    private val ADD_NOTE_TIME_TAG = "AddNoteActivity-addNoteTime"

    private var isFirst = true

    private val format = SimpleDateFormat("yyyy-MM-dd-EE")

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)


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
                binding.endTimeDate.text = format.format(it)
//                var year = SimpleDateFormat("yyyy").format(it)
//                var month = SimpleDateFormat("MM").format(it)
//                var day = SimpleDateFormat("dd").format(it)
//                var weekday = SimpleDateFormat("E").format(it)
//                Log.e("$year $month $day $weekday",END_TIME_TAG)
            }
        }

        binding.addNoteDate.setOnClickListener {
            datePicker.show(this.supportFragmentManager,ADD_NOTE_TIME_TAG)
            datePicker.addOnPositiveButtonClickListener {
                binding.addNoteDate.text = format.format(it)
            }
        }

        // TODO: 2021/4/19 分类事件的点击：弹出弹窗几个分类，新增分类跳转activity 
        binding.chooseSortNoteLayout.setOnClickListener {
            var intent = Intent(this,SortNoteActivity::class.java)
            startActivity(intent)
        }



        setContentView(binding.root)
    }
}