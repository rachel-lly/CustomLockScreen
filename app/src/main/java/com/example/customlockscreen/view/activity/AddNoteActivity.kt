package com.example.customlockscreen.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.customlockscreen.R
import com.example.customlockscreen.util.SharedPreferenceCommission
import com.example.customlockscreen.databinding.ActivityAddNoteBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.util.Code
import com.example.customlockscreen.util.TimeManager.Companion.format
import com.example.customlockscreen.util.ToastUtil.Companion.toast
import com.example.customlockscreen.viewmodel.LabelViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlin.time.ExperimentalTime




class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddNoteBinding

    private val END_TIME_TAG = "END_TIME_TAG"
    private val ADD_NOTE_TIME_TAG = "ADD_NOTE_TIME_TAG"

    private var isFirstEndTime = true

    private var targetDayTime:Long = MaterialDatePicker.todayInUtcMilliseconds()

    private var endTime:Long ?= null

    private val today = format.format(targetDayTime)

    private val labelDao = DataBase.dataBase.labelDao()
    private val sortNoteDao = DataBase.dataBase.sortNoteDao()

    private var lastChoose: String? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val sortNote = data?.getStringExtra(Code.SORT_NOTE_TEXT);
        if(sortNote == null){

            if(lastChoose!=null){
                binding.noteAttributeLayout.chooseSortTv.text = lastChoose
            }else{
                initDefaultSortNoteName()
            }

        }else{
            lastChoose = sortNote
            binding.noteAttributeLayout.chooseSortTv.text = sortNote
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun initDefaultSortNoteName(){
        val sortNote:String
        val sortNoteCnt =  sortNoteDao.getSortNoteCount()
        if(sortNoteCnt==0){
            sortNote = "暂无分类本"
        }else{
            sortNote = sortNoteDao.getAllSortNotes()[0].name
        }
        binding.noteAttributeLayout.chooseSortTv.text = sortNote
    }

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  DataBindingUtil.setContentView(this,R.layout.activity_add_note)

        //ViewModel
        val labelViewModel = ViewModelProvider(this)[LabelViewModel::class.java]
        binding.viewmodel = labelViewModel
        labelViewModel.label.observe(this){

        }

        binding.lifecycleOwner = this


        binding.noteAttributeLayout.addNoteDate.text = today

        initDefaultSortNoteName()

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
            startActivityForResult(intent, Code.RESULT_CODE)
        }


        binding.addNoteSure.setOnClickListener {

            if(binding.noteAttributeLayout.chooseSortTv.text.equals("暂无分类本")){
                this.toast("请先建立事件的分类本")
            }else{
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
                        this.toast("该事件已存在")
                    }else{
                        labelDao.insertLabel(addLabel)
                        this.toast("保存数据成功")
                        finish()
                    }

                }
            }
        }

    }
}