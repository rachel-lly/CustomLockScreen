package com.example.customlockscreen.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.ActivityChangeAvatarBinding
import com.example.customlockscreen.util.Code
import com.example.customlockscreen.util.FileUtil.Companion.saveAvatar
import com.example.customlockscreen.util.ToastUtil.Companion.toast
import com.example.library.PermissionX



class ChangeAvatarActivity : AppCompatActivity() {

    lateinit var binding: ActivityChangeAvatarBinding
    lateinit var bitmap:Bitmap

    val TAG = "ChangeAvatarActivity"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  DataBindingUtil.setContentView(this,R.layout.activity_change_avatar)

        binding.lifecycleOwner = this

        supportActionBar?.setDisplayShowTitleEnabled(false)
        setSupportActionBar(binding.changeAvatarToolbar)

        supportActionBar!!.title = ""


        binding.changeAvatarToolbar.setNavigationIcon(R.mipmap.back)
        binding.changeAvatarToolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }


//        val avatarPath = "${getAvatarCacheDir(this)}/avatar.png"
//
//        if(isExistFile(avatarPath)){
//            val file = File(avatarPath)
//            Glide.with(this).load(file).into(binding.mainAvatar)
//        }else{
//            Glide.with(this).load(R.drawable.avater).into(binding.mainAvatar)
//        }

        Glide.with(this).load(R.drawable.avater).into(binding.mainAvatar)

        binding.changeAvatarButton.setOnClickListener {
            PermissionX.request(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE){ allgranted, deniedList ->

                if(allgranted){
                    //跳转系统相册
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, Code.IMAGE_REQUEST_CODE)

                }else{
                    toast("你拒绝了 $deniedList")
                }

            }
        }




    }

    // TODO: 2021/11/16 处理获取到的图片uri及裁剪后的图片
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(Activity.RESULT_OK == resultCode){

            when (requestCode){
                Code.IMAGE_REQUEST_CODE ->{
                    val uri = data!!.data

                    bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri!!))

                    Glide.with(this).load(bitmap).into(binding.mainAvatar)
                }

                Code.REQUEST_CODE_CAPTURE_CROP ->{

                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.toolbar_sure_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId){
            R.id.sure ->{
                saveAvatar(this,bitmap,"avatar")
                finishAfterTransition()
            }
        }
        return true
    }

    // TODO: 2021/11/16 调用系统裁剪图片 
    private fun gotoCrop(sourceUri: Uri){


        val intent = Intent("com.android.camera.action.CROP")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.apply {
            putExtra("crop","true")
            putExtra("aspectX",1)
            putExtra("aspectY",1)
            putExtra("outputX",256)
            putExtra("outputY",256)
            putExtra("scale",true)
            putExtra("return_data",false)
            putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString())
            setDataAndType(sourceUri,"image/*")
        }

//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.R){
//            intent.putExtra(MediaStore.EXTRA_OUTPUT)
//        }else{
//            val imgCropUri = Uri.fromFile()
//            intent.putExtra(MediaStore.EXTRA_OUTPUT,sourceUri)
//        }
        startActivityForResult(intent, Code.REQUEST_CODE_CAPTURE_CROP)
    }
}