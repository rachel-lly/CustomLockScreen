package com.example.customlockscreen.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.customlockscreen.R
import com.example.customlockscreen.util.SharedPreferenceCommission
import com.example.customlockscreen.databinding.ActivityEditNoteAttributeBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.util.Code
import com.example.customlockscreen.util.TimeManager.Companion.format
import com.example.customlockscreen.util.ToastUtil.Companion.toast
import com.example.customlockscreen.viewmodel.LabelViewModel
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
        var sortNote = data?.getStringExtra(Code.SORT_NOTE_TEXT);
        if(sortNote == null){
            sortNote = lastChoose
        }else{
            lastChoose = sortNote
        }
        binding.noteAttributeLayout.chooseSortTv.text = sortNote
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  DataBindingUtil.setContentView(this,R.layout.activity_edit_note_attribute)

        label = intent!!.getParcelableExtra(Code.LABEL)!!

        //ViewModel
        val labelViewModel = ViewModelProvider(this)[LabelViewModel::class.java]
        labelViewModel.label.value = label
        binding.noteAttributeLayout.viewmodelchild = labelViewModel
        binding.lifecycleOwner = this

        binding.editNoteAttributeToolbar.setNavigationIcon(R.mipmap.back)
        binding.editNoteAttributeToolbar.setNavigationOnClickListener {
            finish()
        }



//
//        binding.noteAttributeLayout.addNoteEt.text = SpannableStringBuilder(label.text)
//        binding.noteAttributeLayout.addNoteEt.setSelection(label.text.length)
//        binding.noteAttributeLayout.addNoteEt.requestFocus()



        val todayTime = MaterialDatePicker.todayInUtcMilliseconds()

        if(label.targetDate<todayTime){
            binding.noteAttributeLayout.addNoteDate.setTextColor(ContextCompat.getColor(this,R.color.color_passed))
        }else{
            binding.noteAttributeLayout.addNoteDate.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
        }

        label.sortNote.also{

            lastChoose = it
        }



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
            binding.noteAttributeLayout.endTimeDate.text = format.format(System.currentTimeMillis())
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
            startActivityForResult(intent, Code.RESULT_CODE)
        }


        binding.editNoteSure.setOnClickListener {

            if(binding.noteAttributeLayout.chooseSortTv.text.equals("暂无分类本")){
                this.toast("请先建立事件的分类本")
            }else{
                val noteText = binding.noteAttributeLayout.addNoteEt.text.toString()
                val lastName = label.text


                if(noteText.isEmpty()){
                    this.toast("事件不能为空")
                }else{

                    label.text = noteText

                    if(endTime!=null){
                        label.isEnd = binding.noteAttributeLayout.endTimeSwitch.isChecked
                        if(label.isEnd){
                            label.endDate = endTime as Long
                        }
                    }

                    label.isTop = binding.noteAttributeLayout.toTopSwitch.isChecked
                    label.targetDate = targetDayTime
                    label.refreshDay()

                    val sortNoteName = binding.noteAttributeLayout.chooseSortTv.text.toString()
                    if(sortNoteName.isNotEmpty()){
                        label.sortNote = sortNoteName
                    }


                    val nameList = labelDao.getAllLabelsName()

                    if(!lastName.equals(noteText)&&nameList.contains(noteText)){
                        this.toast("该事件已存在")
                    }else{
                        if(label.isTop){
                            changeOnTopLabel(label.text)
                        }else if(lastName.equals(noteText)){
                            changeOnTopLabel("-1")
                        }
                        labelDao.updateLabel(label)
                        this.toast("修改数据成功,$label")

                        val intent = Intent()
                        intent.putExtra(Code.IS_DELETE,false)
                        intent.putExtra(Code.LABEL,label)
                        setResult(Code.RESULT_CODE,intent)
                        finish()
                    }

                }
            }


        }

        binding.deleteNoteSure.setOnClickListener {
            labelDao.deleteLabel(label)
            this.toast("删除数据成功")
            val intent = Intent()
            intent.putExtra(Code.IS_DELETE,true)
            setResult(Code.RESULT_CODE,intent)
            finish()
        }


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