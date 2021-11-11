package com.example.customlockscreen.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.customlockscreen.util.FileUtil.Companion.getBitmapCacheDir
import com.example.customlockscreen.util.FileUtil.Companion.saveBitmap
import java.io.*


class PictureUtil{

    private lateinit var bitmapName:String
    private lateinit var bitmapPath:String


    fun shotShare(context: Context, bitmap: Bitmap){

        bitmapName = "share"
        bitmapPath = "${getBitmapCacheDir(context)}/$bitmapName.png"

        saveBitmap(context, bitmap,bitmapName)
        shareImage(context, bitmapPath)

    }

    fun savePictureToPhotoAlbum(context: Context, bitmap: Bitmap){
        bitmapName = "lock"
        bitmapPath = "${getBitmapCacheDir(context)}/$bitmapName.png"
        saveBitmap(context, bitmap,bitmapName)
        putBitmapToMedia(context, bitmapName, bitmap)
    }


    private fun shareImage(context: Context, imagePath: String) {

        val file = File(imagePath)

        val uri = if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(context, "com.example.customlockscreen.fileprovider", file)
        } else {
            Uri.fromFile(file)
        }

        var intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri) // 分享的内容
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享")
        intent = Intent.createChooser(intent, "分享到：")

        context.startActivity(intent)

    }



    fun putBitmapToMedia(context: Context, fileName: String, bm: Bitmap) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        try {
            val out = context.contentResolver.openOutputStream(uri!!)
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out?.flush()
            out?.close()
            Toast.makeText(context, "图片保存成功", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(context, "图片保存失败", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

}