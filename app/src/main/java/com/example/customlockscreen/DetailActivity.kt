package com.example.customlockscreen

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.customlockscreen.databinding.ActivityDetailBinding


const val LABEL_DAY = "LABEL_DAY"
const val LABEL_TEXT = "LABEL_TEXT"
const val LABEL_DATE = "LABEL_DATE"

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)

        setSupportActionBar(binding.detailToolbar)
        binding.detailToolbar.setTitleTextColor(Color.WHITE)

        steepStatusBar()


        binding.detailToolbar.setNavigationIcon(R.mipmap.back)

        binding.detailToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.detailCard.labelText.text = intent?.getStringExtra(LABEL_TEXT)
        binding.detailCard.labelDate.text = intent?.getStringExtra(LABEL_DATE)
        binding.detailCard.labelDay.text = intent?.getStringExtra(LABEL_DAY)


        setContentView(binding.root)
    }

    private fun steepStatusBar() {


        var release=Build.MODEL
        if (release!=null){
            if (release.contains("HUAWEI")){

                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色

            var  decorView = window.decorView
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            var option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            decorView.systemUiVisibility = option
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else {

            var attributes = window.attributes
            var  flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            var  flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            attributes.flags = flagTranslucentStatus
            attributes.flags = flagTranslucentNavigation
            window.attributes = attributes
        }

    }
}