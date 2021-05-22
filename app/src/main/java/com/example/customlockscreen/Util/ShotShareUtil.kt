package com.example.customlockscreen.Util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ShotShareUtil(context: Context){

    companion object{
        fun getBitmapCacheDir(context: Context):String{
            return "${context.getExternalFilesDir(null)?.absolutePath}/BitmapCache"
        }
    }

    private val BITMAP_DIR = "${context.getExternalFilesDir(null)?.absolutePath}/BitmapCache"

    private lateinit var bitmapName:String

    private lateinit var bitmapPath:String


    fun shotShare(context: Context,bitmap: Bitmap){

        bitmapName = "${System.currentTimeMillis()}"

        bitmapPath = "${getBitmapCacheDir(context)}/$bitmapName.png"

        SaveBitmap(bitmap)

        ShareImage(context, bitmapPath)

    }



    private fun ShareImage(context: Context, imagePath: String) {

        val uri: Uri
        val file = File(imagePath)

        if (context == null || file == null) {
            throw NullPointerException()
        }
        uri = if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(context, "com.example.customlockscreen.fileprovider", file)
        } else {
            Uri.fromFile(file)
        }



        var intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri) // 分享的内容
        intent.putExtra(Intent.EXTRA_SUBJECT,"分享")
        intent = Intent.createChooser(intent, "分享到：")

        context.startActivity(intent)


    }

    private fun SaveBitmap(bitmap: Bitmap) {

        try { // 获取SDCard指定目录下
            val sdCardDir = BITMAP_DIR
            val dirFile = File(sdCardDir) //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs()
            }
            val file = File(sdCardDir, "$bitmapName.png") // 在该目录下创建图片
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile() //创建文件
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}