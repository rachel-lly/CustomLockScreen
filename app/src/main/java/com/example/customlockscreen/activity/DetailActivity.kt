package com.example.customlockscreen.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.*
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.customlockscreen.R
import com.example.customlockscreen.Util.PictureUtil
import com.example.customlockscreen.databinding.ActivityDetailBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.db.DataBase
import com.example.library.PermissionX
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*

const val LABEL_TEXT = "LABEL_TEXT"
const val LABEL_IS_LOCK = "LABEL_IS_LOCK"

class DetailActivity : AppCompatActivity() {

    val EVENT_SCREENSHOT_SHARE = 22 //截图分享
    val EVENT_SCREENSHOT_LOCK = 23 //截图设为锁屏

    private var mediaProjectionManager: MediaProjectionManager? = null
    private var mediaProjection: MediaProjection? = null
    private var image: Image? = null

    private lateinit var binding : ActivityDetailBinding

    private var labelIsLock = false


    private val format = SimpleDateFormat("yyyy-MM-dd-EE", Locale.CHINESE)

    private val labelDao = DataBase.dataBase.labelDao()

    private lateinit var label: Label



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

            val labelText = intent!!.getStringExtra(LABEL_TEXT)
            labelIsLock = intent!!.getBooleanExtra(LABEL_IS_LOCK,false)



            label = labelText!!.let { labelDao.getLabelByName(it)!! }

            binding.detailCard.labelText.text = labelText
            binding.detailCard.labelDate.text = format.format(label.targetDate)

            val day = label.day


            binding.detailCard.labelDay.text = Math.abs(day).toString()
            if(day>=0){
                binding.detailCard.labelText.setBackgroundColor(resources.getColor(R.color.note_list_future_dark, theme))
            }else{
                binding.detailCard.labelText.setBackgroundColor(resources.getColor(R.color.note_list_history_dark, theme))
            }



            setContentView(binding.root)
    }

    private fun takeScreenShotToShare() {
        mediaProjectionManager = application.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(mediaProjectionManager!!.createScreenCaptureIntent(), EVENT_SCREENSHOT_SHARE)
    }

    private fun takeScreenShotToLock() {
        mediaProjectionManager = application.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(mediaProjectionManager!!.createScreenCaptureIntent(), EVENT_SCREENSHOT_LOCK)
    }

    @SuppressLint("WrongConstant")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode == EVENT_SCREENSHOT_LOCK or EVENT_SCREENSHOT_SHARE){
            val displayMetrics = DisplayMetrics()
            val windowManager = this.getSystemService(WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            val height = displayMetrics.heightPixels

            val mImageReader: ImageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2)
            mediaProjection = mediaProjectionManager!!.getMediaProjection(resultCode, data!!)
            val virtualDisplay = mediaProjection!!.createVirtualDisplay("screen-mirror", width, height,
                    displayMetrics.densityDpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null)



            Handler(Looper.myLooper()!!).postDelayed({
                try {
                    image = mImageReader.acquireLatestImage()
                    if (image != null) {
                        val planes: Array<Image.Plane> = image!!.getPlanes()
                        val buffer: ByteBuffer = planes[0].getBuffer()
                        val width: Int = image!!.getWidth()
                        val height: Int = image!!.getHeight()

                        val pixelStride: Int = planes[0].getPixelStride()
                        val rowStride: Int = planes[0].getRowStride()
                        val rowPadding = rowStride - pixelStride * width
                        var bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888)
                        bitmap!!.copyPixelsFromBuffer(buffer)
                        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, false)
                        if (bitmap != null) {

                            if (requestCode == EVENT_SCREENSHOT_SHARE) {
                                cutScreenShotToShare(bitmap)
                            }else if(requestCode == EVENT_SCREENSHOT_LOCK){
                                cutScreenShotToLock(bitmap)
                            }



                        }
                        bitmap.recycle()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    image?.close()
                    mImageReader.close()
                    virtualDisplay?.release()
                    //必须代码，否则出现BufferQueueProducer: [ImageReader] dequeueBuffer: BufferQueue has been abandoned
                    mImageReader.setOnImageAvailableListener(null, null)
                    mediaProjection!!.stop()
                }
            }, 100)
        }




    }

    private fun steepStatusBar() {


        val release= Build.MODEL
        if (release!=null){
            if (release.contains("HUAWEI")){

                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色

            var  decorView = window.decorView
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
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
        if(!labelIsLock){
            menuInflater.inflate(R.menu.activity_detail_menu, menu)
        }else{
            menuInflater.inflate(R.menu.activity_setting_lock_screen_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when(item.itemId){
            R.id.edit -> {
                val intent = Intent(this, EditNoteAttributeActivity::class.java)
                intent.putExtra(LABEL,label)
                startActivity(intent)
            }

            R.id.share -> {

                PermissionX.request(this,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE){
                    allGranted,deniedList ->
                    run {
                        if (allGranted) {
                            takeScreenShotToShare()
                        } else {
                            Toast.makeText(this,"你拒绝了 $deniedList",Toast.LENGTH_SHORT).show()
                        }
                    }
                }


            }

            R.id.lock_screen -> {


                PermissionX.request(this,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE){
                    allGranted,deniedList ->
                    run {
                        if (allGranted) {
                            takeScreenShotToLock()
                        } else {
                            Toast.makeText(this,"你拒绝了 $deniedList",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }

        return true
    }



    private fun cutScreenShotToShare(bitmap: Bitmap) {

        val screenShot = getScreenShot(bitmap)


        PictureUtil().shotShare(this, screenShot)

    }

    private fun cutScreenShotToLock(bitmap: Bitmap) {

        val screenShot = getScreenShot(bitmap)
        PictureUtil().savePictureToPhotoAlbum(this,screenShot)


        MaterialAlertDialogBuilder(this)
                .setTitle("锁屏设置")
                .setMessage("已保存图片至相册，可在相册中自行设置为锁屏")
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->

                }
                .show()

    }


    private fun getScreenShot(bitmap: Bitmap):Bitmap{
        val screenView = window.decorView


        //获取状态栏高度
        val frame = Rect()
        screenView.getWindowVisibleDisplayFrame(frame)
        val statusbarHeight = frame.top

        //获取标题栏高度(toolbar里有menu，需要截掉)
        val typeValue = TypedValue()
        theme.resolveAttribute(android.R.attr.actionBarSize, typeValue, true)
        val toolbarHeight = TypedValue.complexToDimensionPixelSize(typeValue.data, resources.displayMetrics)


        //获取屏幕长宽
        val width = screenView.width
        val height = screenView.height

        //去掉状态栏和标题栏
        val screenShot = bitmap.let { Bitmap.createBitmap(it, 0, toolbarHeight + statusbarHeight, width, height - toolbarHeight - statusbarHeight) }

        return screenShot;
    }



}


