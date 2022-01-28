package com.example.customlockscreen.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.ActivityBackupDataBinding
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.model.db.LabelDao
import com.example.customlockscreen.model.db.SortNoteDao
import com.example.customlockscreen.util.FileUtil.Companion.deleteDirectory
import com.example.customlockscreen.util.FileUtil.Companion.getBitmapCacheDir
import com.example.customlockscreen.util.FileUtil.Companion.isExistFile
import com.example.customlockscreen.util.ToastUtil.Companion.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BackupDataActivity : AppCompatActivity() {


    private lateinit var binding :ActivityBackupDataBinding

    private lateinit var labelDao: LabelDao

    private lateinit var sortNoteDao: SortNoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBackupDataBinding.inflate(layoutInflater)

        val slide = Slide()
        slide.slideEdge = Gravity.LEFT
        slide.excludeTarget(android.R.id.statusBarBackground, true)
        window.exitTransition = slide


        val slide2 = Slide()
        slide2.slideEdge = Gravity.RIGHT
        slide2.excludeTarget(android.R.id.statusBarBackground, true)
        window.enterTransition = slide2

        labelDao  = DataBase.dataBase.labelDao()
        sortNoteDao = DataBase.dataBase.sortNoteDao()

        binding.backupDataToolbar.setNavigationIcon(R.mipmap.back)
        binding.backupDataToolbar.setNavigationOnClickListener {
            finishAfterTransition()
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
                    deleteAllSortNote()
                }
                .setNegativeButton(resources.getString(R.string.decline)){dialog,which ->

                }
                .show()
    }

    private fun deleteAllSortNote() {

        if (sortNoteDao.getSortNoteCount() == 0){
            this.toast("当前无分类本")
        }else{
            labelDao.deleteAllLabel()
            sortNoteDao.deleteAllSortNote()
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

        if (labelDao.getLabelCount() == 0){
            this.toast("当前无事件记录")
        }else{
            labelDao.deleteAllLabel()
            this.toast("已清空所有事件")
        }
    }

    private fun showDeletePictureDialog() {

        val bitmapDir = getBitmapCacheDir(this)

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

        val isExist = isExistFile(bitmapDir)
        if(!isExist){
            this.toast("暂无缓存图片")
        }else{
            deleteDirectory(bitmapDir)
            this.toast("清除缓存图片成功")
        }
    }

}