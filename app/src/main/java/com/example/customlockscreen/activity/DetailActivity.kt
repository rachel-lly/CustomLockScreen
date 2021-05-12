package com.example.customlockscreen.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.view.drawToBitmap
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_detail_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        // TODO: 2021/5/10 详细事件页面 分享功能
        when(item.itemId){
            R.id.edit->{
                val intent = Intent(this,EditNoteAttributeActivity::class.java)
                startActivity(intent)
            }

            R.id.share->{

                screenShot()

            }
        }

        return true
    }

    private fun screenShot() {
        //获取全屏截图（包括状态栏、标题栏和底部）
        val screenView = window.decorView
        val bitmap = screenView.drawToBitmap()

        //获取状态栏高度
        val frame = Rect()
        screenView.getWindowVisibleDisplayFrame(frame)
        val statusbarHeight = frame.top

        //获取标题栏高度(toolbar里有menu，需要截掉)
        val typeValue = TypedValue()
        theme.resolveAttribute(android.R.attr.actionBarSize,typeValue,true)
        val toolbarHeight = TypedValue.complexToDimensionPixelSize(typeValue.data,resources.displayMetrics)


        //获取屏幕长宽
        val width = screenView.width
        val height = screenView.height

        //去掉状态栏和标题栏
        val screenShot = Bitmap.createBitmap(bitmap,0,toolbarHeight+statusbarHeight,width,height-toolbarHeight-statusbarHeight)

    }


}