package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.customlockscreen.R
import com.example.customlockscreen.Util.ShotShareUtil
import com.example.customlockscreen.databinding.ActivityBackupDataBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class BackupDataActivity : AppCompatActivity() {


    private lateinit var binding :ActivityBackupDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBackupDataBinding.inflate(layoutInflater)


        binding.backupDataToolbar.setNavigationIcon(R.mipmap.back)
        binding.backupDataToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.backupPictureLayout.setOnClickListener {
            showDeleteDialog()
        }


        setContentView(binding.root)
    }

    private fun showDeleteDialog() {

            val bitmapDir = ShotShareUtil.getBitmapCacheDir(this)

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
        delete(fileDir)
        fileDir.delete()
        if(!fileDir.exists()){
            Toast.makeText(this,"清除缓存图片成功",Toast.LENGTH_SHORT).show()
        }

    }

    private fun delete(fileDir:File){

        val files = fileDir.listFiles()

        if(files!=null){
            for(index in 0..files.size-1){
                var deleteFile = files[index]
                if(deleteFile.isFile){
                    deleteFile.delete()
                }else if(deleteFile.isDirectory){
                    delete(deleteFile)
                }
                deleteFile.delete()
            }
        }


    }


}