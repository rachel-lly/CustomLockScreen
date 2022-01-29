package com.example.customlockscreen.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.customlockscreen.R
import com.example.customlockscreen.adapter.IconListAdapter
import com.example.customlockscreen.databinding.ActivityEditSortNoteBinding
import com.example.customlockscreen.model.bean.SortNote
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.util.Code
import com.example.customlockscreen.util.ToastUtil.Companion.toast
import com.example.customlockscreen.viewmodel.SortNoteViewModel


class EditSortNoteActivity : AppCompatActivity() {

    private var mPosition:Int = 0

    private lateinit var binding : ActivityEditSortNoteBinding

    private lateinit var clickListener: IconListAdapter.ClickListener

    private val sortNoteDao = DataBase.dataBase.sortNoteDao()

    private val labelDao = DataBase.dataBase.labelDao()

    private lateinit var adapter: IconListAdapter

    private var sortNote:SortNote? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  DataBindingUtil.setContentView(this,R.layout.activity_edit_sort_note)

        sortNote = intent?.getParcelableExtra(Code.SORT_NOTE)
        //ViewModel
        val sortNoteViewModel = ViewModelProvider(this)[SortNoteViewModel::class.java]
        sortNoteViewModel.sortNote.value = sortNote
        binding.editSortNoteCard.viewmodelchild = sortNoteViewModel

        sortNoteViewModel.sortNote.observe(this){

        }
        binding.lifecycleOwner = this

        binding.editSortNoteToolbar.setNavigationIcon(R.mipmap.back)
        binding.editSortNoteToolbar.setNavigationOnClickListener {
            finish()
        }


        val size = Code.iconList.size
        for(i in 0 until size){
            val s = resources.getResourceEntryName(Code.iconList[i])
            if(s.equals(sortNote!!.iconName)){
                mPosition = i
                break;
            }
        }




        if (sortNote != null) {
            binding.editSortNoteCard.addSortNoteEt.text = SpannableStringBuilder(sortNote!!.name)
            binding.editSortNoteCard.addSortNoteEt.setSelection(sortNote!!.name.length)
            binding.editSortNoteCard.addSortNoteEt.requestFocus()
        }


        clickListener = object :IconListAdapter.ClickListener{
            override fun onClick(position: Int) {
                mPosition = position
            }
        }


        adapter = IconListAdapter(this,clickListener,mPosition)
        val layoutManager = GridLayoutManager(this,6)


        binding.editSortNoteCard.recycleView.adapter = adapter
        binding.editSortNoteCard.recycleView.layoutManager = layoutManager


        binding.editNoteSure.setOnClickListener {
            updateSortNote()
        }

        binding.editNoteDelete.setOnClickListener {

            if(sortNote!=null){

                if(labelDao.getSameSortNoteLabelList(sortNote!!.name).isNotEmpty()){
                    this.toast("该分类本下有事件，删除失败")
                }else{
                    sortNoteDao.deleteSortNote(sortNote!!)
                    this.toast("删除分类本成功")
                    finish()
                }

            }
        }

    }

    private fun updateSortNote() {

        val iconName = resources.getResourceEntryName(Code.iconList[mPosition])
        if(binding.editSortNoteCard.addSortNoteEt.text.isEmpty()){
            this.toast("分类本文字不能为空")
        }else{
            val sortNoteName = binding.editSortNoteCard.addSortNoteEt.text.toString()

            val lastName = sortNote!!.name

            sortNote!!.name = sortNoteName
            sortNote!!.iconName = iconName
            this.toast("${sortNote}")
            val nameList = sortNoteDao.getAllSortNotesName()


            if(!lastName.equals(sortNote!!.name)&&nameList.contains(sortNoteName)){
                this.toast("该分类本已存在")
            }else{
                sortNoteDao.updateSortNote(sortNote!!)
                val list = labelDao.getSameSortNoteLabelList(lastName)
                for(label in list){
                    labelDao.updateLabelBySortNote(sortNoteName,label.id)
                }
                this.toast("修改数据成功")
                finish()
            }
        }

    }
}