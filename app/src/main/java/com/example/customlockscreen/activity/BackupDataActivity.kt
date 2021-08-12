package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customlockscreen.R
import com.example.customlockscreen.util.PictureUtil
import com.example.customlockscreen.databinding.ActivityBackupDataBinding
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.model.db.LabelDao
import com.example.customlockscreen.model.db.SortNoteDao
import com.example.customlockscreen.util.ToastUtil.Companion.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class BackupDataActivity : AppCompatActivity() {


    private lateinit var binding :ActivityBackupDataBinding

    private lateinit var labelDao: LabelDao

    private lateinit var sortNoteDao: SortNoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBackupDataBinding.inflate(layoutInflater)

        labelDao  = DataBase.dataBase.labelDao()
        sortNoteDao = DataBase.dataBase.sortNoteDao()

        binding.backupDataToolbar.setNavigationIcon(R.mipmap.back)
        binding.backupDataToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.backupPictureLayout.setOnClickListener {
            showDeletePictureDialog()
        }

        binding.backupLabelLayout.setOnClickListener {
            showDeleteLabelDialog()
        }

        binding.backupSortnoteLayout.setOnClickListener {
            showDeleteSortNoteDialog()
        }


        setContentView(binding.root)
    }

    private fun showDeleteSortNoteDialog() {
        MaterialAlertDialogBuilder(this)
                .setTitle("确定清空所有分类本吗？")
                .setMessage("删除分类本之前会删除所有分类本下的事件记录")
                .setPositiveButton(resources.getString(R.string.accept)){ dialog,which ->
                    deleteAllLabel()
                    deleteAllSortNote()
                }
                .setNegativeButton(resources.getString(R.string.decline)){dialog,which ->

                }
                .show()
    }

    private fun deleteAllSortNote() {
        val sortNoteList = sortNoteDao.getAllSortNotes()
        if (sortNoteList.isEmpty()){
            this.toast("当前无分类本")
        }else{
            for(sortNote in sortNoteList){
                sortNoteDao.deleteSortNote(sortNote)
            }
            this.toast("已清空所有分类本及分类本下的事件")
        }

    }

    private fun showDeleteLabelDialog() {
        MaterialAlertDialogBuilder(this)
                .setTitle("确定清空所有事件吗？")
                .setMessage("重置所有事件记录")
                .setPositiveButton(resources.getString(R.string.accept)){ dialog,which ->
                    deleteAllLabel()
                }
                .setNegativeButton(resources.getString(R.string.decline)){dialog,which ->

                }
                .show()
    }

    private fun deleteAllLabel() {
        val labelList = labelDao.getAllLabels()
        if (labelList.isEmpty()){
            this.toast("当前无事件记录")
        }else{
            for(label in labelDao.getAllLabels()) {
                labelDao.deleteLabel(label)
            }
            this.toast("已清空所有事件")
        }
    }

    private fun showDeletePictureDialog() {

        val bitmapDir = PictureUtil.getBitmapCacheDir(this)

        MaterialAlertDialogBuilder(this)
                .setTitle("确定清空缓存文件吗？")
                .setMessage("缓存图片储存文件路径为:$bitmapDir")
                .setPositiveButton(resources.getString(R.string.accept)){ dialog,which ->
                    deleteAllFile(bitmapDir)
                }
                .setNegativeButton(resources.getString(R.string.decline)){dialog,which ->

                }
                .show()

    }

    private fun deleteAllFile(bitmapDir: String) {

        val fileDir = File(bitmapDir)
        if(!fileDir.exists()){
            this.toast("暂无缓存图片")
        }else{
            delete(fileDir)
            fileDir.delete()
            if(!fileDir.exists()){
                this.toast("清除缓存图片成功")
            }
        }

    }

    private fun delete(fileDir:File){

        val files = fileDir.listFiles()

        if(files!=null){
            for(file in files){
                if(file.isFile){
                    file.delete()
                }else if(file.isDirectory){
                    delete(file)
                }
                file.delete()
            }
        }

    }

}