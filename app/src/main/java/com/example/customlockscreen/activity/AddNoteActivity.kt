package com.example.customlockscreen.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.customlockscreen.R
import com.example.customlockscreen.util.SharedPreferenceCommission
import com.example.customlockscreen.databinding.ActivityAddNoteBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.db.DataBase
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.ExperimentalTime

const val SORT_NOTE_TEXT = "SORT_NOTE_TEXT"
const val RESULT_CODE = 0


class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddNoteBinding

    private val END_TIME_TAG = "END_TIME_TAG"
    private val ADD_NOTE_TIME_TAG = "ADD_NOTE_TIME_TAG"

    private var isFirstEndTime = true

    private val format = SimpleDateFormat("yyyy-MM-dd-EE", Locale.CHINESE)

    private var targetDayTime:Long = MaterialDatePicker.todayInUtcMilliseconds()

    private var endTime:Long ?= null

    private val today = format.format(targetDayTime)

    private val labelDao = DataBase.dataBase.labelDao()

    private var lastChoose: String? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var sortNote = data?.getStringExtra(SORT_NOTE_TEXT);
        if(sortNote == null){

            sortNote = if (lastChoose != null)  lastChoose  else  "生活"

        }else{
            lastChoose = sortNote
        }
        binding.noteAttributeLayout.chooseSortTv.text = sortNote
        super.onActivityResult(requestCode, resultCode, data)
    }

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)

        binding.noteAttributeLayout.addNoteDate.text = today

        val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener {

            val chooseDay = format.format(it)

            when(datePicker.tag){

                END_TIME_TAG ->{

                    endTime = it

                    binding.noteAttributeLayout.endTimeDate.text = chooseDay
                    if(chooseDay<today){
                        binding.noteAttributeLayout.endTimeDate.setTextColor(ContextCompat.getColor(this,R.color.color_passed))
                    }else{
                        binding.noteAttributeLayout.endTimeDate.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                    }
                }

                ADD_NOTE_TIME_TAG ->{

                    targetDayTime = it

                    binding.noteAttributeLayout.addNoteDate.text = chooseDay
                    if(chooseDay<today){
                        binding.noteAttributeLayout.addNoteDate.setTextColor(ContextCompat.getColor(this,R.color.color_passed))
                    }else{
                        binding.noteAttributeLayout.addNoteDate.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
                    }
                }
            }

        }


        binding.noteAttributeLayout.endTimeDate.visibility = View.GONE

        setSupportActionBar(binding.addNoteToolbar)
        binding.addNoteToolbar.setNavigationIcon(R.mipmap.back)
        binding.addNoteToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.noteAttributeLayout.endTimeSwitch.setOnClickListener {

            if(isFirstEndTime){
                binding.noteAttributeLayout.endTimeDate.visibility = View.VISIBLE
                isFirstEndTime = false
            }else{
                binding.noteAttributeLayout.endTimeDate.visibility = View.GONE
                isFirstEndTime = true
            }

        }


        binding.noteAttributeLayout.endTimeDate.setOnClickListener {
           datePicker.show(this.supportFragmentManager,END_TIME_TAG)
        }

        binding.noteAttributeLayout.addNoteDate.setOnClickListener {
            datePicker.show(this.supportFragmentManager,ADD_NOTE_TIME_TAG)
        }


        binding.noteAttributeLayout.chooseSortNoteLayout.setOnClickListener {
            val intent = Intent(this, SortNoteActivity::class.java)
            startActivityForResult(intent, RESULT_CODE)
        }


        binding.addNoteSure.setOnClickListener {

            val noteText = binding.noteAttributeLayout.addNoteEt.text.toString()
            if(noteText.isEmpty()){
                Toast.makeText(this,"事件不能为空",Toast.LENGTH_SHORT).show()
            }else{

                val todayTime = MaterialDatePicker.todayInUtcMilliseconds()
                val addLabel = Label(noteText,targetDayTime,todayTime)

                if(endTime!=null){
                    addLabel.isEnd = binding.noteAttributeLayout.endTimeSwitch.isChecked
                    if(addLabel.isEnd){
                        addLabel.endDate = endTime as Long
                    }
                }

                addLabel.isTop = binding.noteAttributeLayout.toTopSwitch.isChecked

                var topLabelName by SharedPreferenceCommission(this,"topLabelName","-1")
                if(addLabel.isTop){

                    if(!topLabelName.equals("-1")){
                        val deleteOnTopLabel = labelDao.getLabelByName(topLabelName)
                        if(deleteOnTopLabel!=null){
                            deleteOnTopLabel.isTop = false
                            labelDao.updateLabel(deleteOnTopLabel)
                        }
                    }
                    topLabelName = addLabel.text
                }

                val sortNoteName = binding.noteAttributeLayout.chooseSortTv.text.toString()
                if(sortNoteName.isNotEmpty()){
                    addLabel.sortNote = sortNoteName
                }

                val nameList = labelDao.getAllLabelsName()

                if(nameList.contains(noteText)){
                    Toast.makeText(this,"该事件已存在",Toast.LENGTH_SHORT).show()
                }else{
                    labelDao.insertLabel(addLabel)
                    Toast.makeText(this,"保存数据成功",Toast.LENGTH_SHORT).show()
                    finish()
                }

            }

        }

        setContentView(binding.root)
    }
}