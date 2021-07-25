package com.example.customlockscreen.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.customlockscreen.R
import com.example.customlockscreen.Util.SharedPreferenceCommission
import com.example.customlockscreen.databinding.ActivityEditNoteAttributeBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.db.DataBase
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

const val LABEL = "LABEL"


class EditNoteAttributeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityEditNoteAttributeBinding

    private val END_TIME_TAG = "END_TIME_TAG"
    private val ADD_NOTE_TIME_TAG = "ADD_NOTE_TIME_TAG"

    private var isFirst = true

    private val format = SimpleDateFormat("yyyy-MM-dd-EE", Locale.CHINA)

    private var targetDayTime:Long = MaterialDatePicker.todayInUtcMilliseconds()

    private var endTime:Long ?= null

    private val today = format.format(targetDayTime)

    private val labelDao = DataBase.dataBase.labelDao()

    private lateinit var label:Label


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.noteAttributeLayout.chooseSortTv.text = data?.getStringExtra(SORT_NOTE_TEXT)
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditNoteAttributeBinding.inflate(layoutInflater)

        binding.editNoteAttributeToolbar.setNavigationIcon(R.mipmap.back)
        binding.editNoteAttributeToolbar.setNavigationOnClickListener {
            finish()
        }

        label = intent!!.getParcelableExtra(LABEL)!!

        targetDayTime = label.targetDate

        binding.noteAttributeLayout.addNoteEt.text = SpannableStringBuilder(label.text)

        binding.noteAttributeLayout.addNoteDate.text = format.format(label.targetDate)

        binding.noteAttributeLayout.chooseSortTv.text = label.sortNote

        binding.noteAttributeLayout.toTopSwitch.isChecked = label.isTop


        binding.noteAttributeLayout.endTimeSwitch.isChecked = label.isEnd

        if(label.isEnd){
            endTime = label.endDate
            binding.noteAttributeLayout.endTimeDate.text = format.format(label.endDate)
            binding.noteAttributeLayout.endTimeDateLayout.visibility = View.VISIBLE
            isFirst = false
        }else{
            binding.noteAttributeLayout.endTimeDate.visibility = View.GONE
        }



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


        binding.noteAttributeLayout.endTimeSwitch.setOnClickListener {

            if(isFirst){
                binding.noteAttributeLayout.endTimeDate.visibility = View.VISIBLE
                isFirst = false
            }else{
                binding.noteAttributeLayout.endTimeDate.visibility = View.GONE
                isFirst = true
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


        binding.editNoteSure.setOnClickListener {

            val noteText = binding.noteAttributeLayout.addNoteEt.text.toString()
            if(noteText.isEmpty()){
                Toast.makeText(this,"事件不能为空", Toast.LENGTH_SHORT).show()
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



                val sortNoteName = binding.noteAttributeLayout.chooseSortTv.text.toString()
                if(!sortNoteName.isEmpty()){
                    addLabel.sortNote = sortNoteName
                }

                if(label.text.equals(addLabel.text)){
                    if(addLabel.isTop){
                        changeOnTopLabel(addLabel.text)
                    }else{
                        changeOnTopLabel("-1")
                    }
                    labelDao.updateLabel(addLabel)
                    Toast.makeText(this,"修改数据成功", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    val nameList = labelDao.getAllLabelsName()

                    var flag = false

                    for(name in nameList){
                        if(name.equals(noteText)){
                            flag = true
                            break
                        }
                    }

                    if(flag){
                        Toast.makeText(this,"该事件已存在", Toast.LENGTH_SHORT).show()
                    }else{
                        if(addLabel.isTop){
                            changeOnTopLabel(addLabel.text)
                        }
                        labelDao.deleteLabel(label)
                        labelDao.insertLabel(addLabel)
                        Toast.makeText(this,"修改数据成功", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                }

            }

        }

        binding.deleteNoteSure.setOnClickListener {
            labelDao.deleteLabel(label)
            Toast.makeText(this,"删除数据成功", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }


    fun changeOnTopLabel(labelName : String){

        var topLabelName by SharedPreferenceCommission(this,"topLabelName","-1")

        val deleteOnTopLabel = labelDao.getLabelByName(topLabelName)
        if(deleteOnTopLabel!=null){
            deleteOnTopLabel.isTop = false
            labelDao.updateLabel(deleteOnTopLabel)
        }
        topLabelName = labelName
    }
}