package com.example.customlockscreen.activity

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.ActivityDetailBinding
import java.text.SimpleDateFormat

const val LABEL_DAY = "LABEL_DAY"
const val LABEL_TEXT = "LABEL_TEXT"
const val LABEL_DATE = "LABEL_DATE"

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding

    private val format = SimpleDateFormat("yyyy-MM-dd-EE")


    @RequiresApi(Build.VERSION_CODES.M)
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

        var date = intent?.getLongExtra(LABEL_DATE,0)
        var day = intent?.getLongExtra(LABEL_DAY,0)
        if(date!=null){
            binding.detailCard.labelDate.text = format.format(date)
        }


        if (day != null) {
            binding.detailCard.labelDay.text = Math.abs(day).toString()
            if(day>=0){

                binding.detailCard.labelText.setBackgroundColor(resources.getColor(R.color.note_list_future_dark,theme))
            }else{

                binding.detailCard.labelText.setBackgroundColor(resources.getColor(R.color.note_list_history_dark,theme))
            }
        }









        setContentView(binding.root)
    }

    private fun steepStatusBar() {


        var release= Build.MODEL
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