package com.example.customlockscreen.util

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class FileUtil {

    companion object{

        fun getBitmapCacheDir(context: Context) = "${context.getExternalFilesDir(null)?.absolutePath}/BitmapCache"
        fun getBitmapAvatarDir(context: Context) = "${context.getExternalFilesDir(null)?.absolutePath}/AvatarCache"


        fun deleteDirectory(dir: String){

            val fileDir = File(dir)
            if(fileDir.exists()){
                delete(fileDir)
                fileDir.delete()
            }

        }

        fun delete(fileDir:File){

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

        fun isExistFile(dir: String) = File(dir).exists()

        fun saveBitmap(context: Context, bitmap: Bitmap,bitmapName: String){
            saveBitmap(bitmap, getBitmapCacheDir(context),bitmapName)
        }

        fun saveAvatar(context: Context, bitmap: Bitmap,bitmapName: String){
            saveBitmap(bitmap, getBitmapAvatarDir(context),bitmapName)
        }

        fun saveBitmap(bitmap: Bitmap,bitmapDir: String,bitmapName: String) {

            try { // 获取SDCard指定目录下

                val dirFile = File(bitmapDir) //目录转化成文件夹
                if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                    dirFile.mkdirs()
                }
                val file = File(bitmapDir, "$bitmapName.png") // 在该目录下创建图片
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


}