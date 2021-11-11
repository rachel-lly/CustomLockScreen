package com.example.customlockscreen.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.ActivityChangeAvatarBinding
import com.example.customlockscreen.fragment.IMAGE_REQUEST_CODE
import com.example.customlockscreen.util.FileUtil.Companion.saveAvatar
import com.example.customlockscreen.util.ToastUtil.Companion.toast
import com.example.library.PermissionX

class ChangeAvatarActivity : AppCompatActivity() {

    lateinit var binding: ActivityChangeAvatarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangeAvatarBinding.inflate(layoutInflater)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.changeAvatarToolbar.setNavigationIcon(R.mipmap.back)
        binding.changeAvatarToolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }

        binding.changeAvatarButton.setOnClickListener {
            PermissionX.request(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE){ allgranted, deniedList ->

                if(allgranted){
                    //跳转系统相册
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, IMAGE_REQUEST_CODE)

                }else{
                    toast("你拒绝了 $deniedList")
                }

            }
        }


        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(Activity.RESULT_OK == resultCode){
            if(IMAGE_REQUEST_CODE == requestCode){
                val uri = data!!.data

                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri!!))

                saveAvatar(this,bitmap,"avatar")

                Glide.with(this).load(bitmap).into(binding.mainAvatar)
            }
        }
    }
}