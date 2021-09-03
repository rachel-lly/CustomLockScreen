package com.example.customlockscreen.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.content.ContextCompat
import com.example.customlockscreen.R
import com.example.customlockscreen.util.SharedPreferenceCommission
import com.example.customlockscreen.databinding.ActivityEditNoteAttributeBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.util.TimeManager.Companion.format
import com.example.customlockscreen.util.ToastUtil.Companion.toast
import com.google.android.material.datepicker.MaterialDatePicker

class EditNoteAttributeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityEditNoteAttributeBinding

    private val END_TIME_TAG = "END_TIME_TAG"
    private val ADD_NOTE_TIME_TAG = "ADD_NOTE_TIME_TAG"

    private var isFirst = true

    private var targetDayTime:Long = MaterialDatePicker.todayInUtcMilliseconds()

    private var endTime:Long ?= null

    private val today = format.format(targetDayTime)

    private val labelDao = DataBase.dataBase.labelDao()

    private lateinit var label:Label

    private var lastChoose: String? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var sortNote = data?.getStringExtra(SORT_NOTE_TEXT)
        if(sortNote == null) {

            sortNote = if (lastChoose != null)  lastChoose  else  "生活"

        }else{
            lastChoose = sortNote
        }
        binding.noteAttributeLayout.chooseSortTv.text = sortNote
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

        val todayTime = MaterialDatePicker.todayInUtcMilliseconds()

        if(label.targetDate<todayTime){
            binding.noteAttributeLayout.addNoteDate.setTextColor(ContextCompat.getColor(this,R.color.color_passed))
        }else{
            binding.noteAttributeLayout.addNoteDate.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
        }

        binding.noteAttributeLayout.chooseSortTv.text = label.sortNote

        binding.noteAttributeLayout.toTopSwitch.isChecked = label.isTop


        binding.noteAttributeLayout.endTimeSwitch.isChecked = label.isEnd

        if(label.isEnd){
            endTime = label.endDate
            binding.noteAttributeLayout.endTimeDate.text = format.format(label.endDate)
            binding.noteAttributeLayout.endTimeDateLayout.visibility = View.VISIBLE

            if(label.endDate<todayTime){
                binding.noteAttributeLayout.endTimeDate.setTextColor(ContextCompat.getColor(this,R.color.color_passed))
            }else{
                binding.noteAttributeLayout.endTimeDate.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            }

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
                this.toast("事件不能为空")
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
                if(sortNoteName.isNotEmpty()){
                    addLabel.sortNote = sortNoteName
                }

                if(label.text.equals(addLabel.text)){
                    if(addLabel.isTop){
                        changeOnTopLabel(addLabel.text)
                    }else{
                        changeOnTopLabel("-1")
                    }
                    labelDao.updateLabel(addLabel)
                    this.toast("修改数据成功")
                    finish()
                }else{
                    val nameList = labelDao.getAllLabelsName()

                    if(nameList.contains(noteText)){
                        this.toast("该事件已存在")
                    }else{
                        if(addLabel.isTop){
                            changeOnTopLabel(addLabel.text)
                        }
                        labelDao.deleteLabel(label)
                        labelDao.insertLabel(addLabel)
                        this.toast("修改数据成功")
                        finish()
                    }
                }
            }
        }

        binding.deleteNoteSure.setOnClickListener {
            labelDao.deleteLabel(label)
            this.toast("删除数据成功")
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }


    private fun changeOnTopLabel(labelName : String){

        var topLabelName by SharedPreferenceCommission(this,"topLabelName","-1")

        val deleteOnTopLabel = labelDao.getLabelByName(topLabelName)
        if(deleteOnTopLabel!=null){
            deleteOnTopLabel.isTop = false
            labelDao.updateLabel(deleteOnTopLabel)
        }
        topLabelName = labelName
    }
}