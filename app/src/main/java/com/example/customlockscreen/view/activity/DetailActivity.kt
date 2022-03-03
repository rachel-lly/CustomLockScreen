package com.example.customlockscreen.view.activity

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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.customlockscreen.R
import com.example.customlockscreen.util.PictureUtil
import com.example.customlockscreen.databinding.ActivityDetailBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.util.Code
import com.example.customlockscreen.util.ToastUtil.Companion.toast
import com.example.customlockscreen.viewmodel.DataViewModel
import com.example.library.PermissionX
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.Exception
import java.nio.ByteBuffer


class DetailActivity : AppCompatActivity() {



    private val TAG = "DetailActivity"

    private lateinit var dataViewModel: DataViewModel

    private var mediaProjectionManager: MediaProjectionManager? = null
    private var mediaProjection: MediaProjection? = null
    private var image: Image? = null

    private lateinit var binding : ActivityDetailBinding

    private var labelIsLock = false

    private lateinit var label: Label


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail)

        label= intent.getParcelableExtra(Code.LABEL)!!
        labelIsLock = intent!!.getBooleanExtra(Code.LABEL_IS_LOCK, false)
        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]

        binding.detailCard.label = label
        dataViewModel.getLabelByid(label.id).observe(this,{
            binding.detailCard.label = it
        })
        binding.lifecycleOwner = this


        setSupportActionBar(binding.detailToolbar)
        binding.detailToolbar.setTitleTextColor(Color.WHITE)

        steepStatusBar()

        binding.detailToolbar.setNavigationIcon(R.mipmap.back)
        binding.detailToolbar.setNavigationOnClickListener {
            finish()
        }

        freshLabel(label)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun freshLabel(label: Label){
        binding.detailCard.label = label

        binding.detailCard.labelDay.updatePadding(0, 25, 0, 25)

        if(label.day>=0){
            binding.detailCard.labelText.setBackgroundColor(resources.getColor(R.color.note_list_future_dark, theme))
        }else{
            binding.detailCard.labelText.setBackgroundColor(resources.getColor(R.color.note_list_history_dark, theme))
        }
    }

    private fun takeScreenShotToShare() {
        mediaProjectionManager = application.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(mediaProjectionManager!!.createScreenCaptureIntent(), Code.EVENT_SCREENSHOT_SHARE)
    }

    private fun takeScreenShotToLock() {
        mediaProjectionManager = application.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(mediaProjectionManager!!.createScreenCaptureIntent(), Code.EVENT_SCREENSHOT_LOCK)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){

            Code.EVENT_SCREENSHOT_LOCK,Code.EVENT_SCREENSHOT_SHARE->{
                val displayMetrics = resources.displayMetrics
                val width = displayMetrics.widthPixels
                val height = displayMetrics.heightPixels

                val mImageReader: ImageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2)
                mediaProjection = mediaProjectionManager!!.getMediaProjection(resultCode, data!!)
                val virtualDisplay = mediaProjection!!.createVirtualDisplay("screen-mirror", width, height,
                    displayMetrics.densityDpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.surface, null, null)

                Handler(Looper.myLooper()!!).postDelayed({
                    try {
                        image = mImageReader.acquireLatestImage()
                        if (image != null) {
                            val planes: Array<Image.Plane> = image!!.planes
                            val buffer: ByteBuffer = planes[0].buffer
                            val width: Int = image!!.width
                            val height: Int = image!!.height
                            val pixelStride: Int = planes[0].pixelStride
                            val rowStride: Int = planes[0].rowStride
                            val rowPadding = rowStride - pixelStride * width
                            var bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888)
                            bitmap!!.copyPixelsFromBuffer(buffer)
                            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, false)
                            if (bitmap != null) {

                                if (requestCode == Code.EVENT_SCREENSHOT_SHARE) {
                                    cutScreenShotToShare(bitmap)
                                }else if(requestCode == Code.EVENT_SCREENSHOT_LOCK){
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
            val decorView = window.decorView
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else {

            val attributes = window.attributes
            val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            val flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
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
                intent.putExtra(Code.LABEL, label.id)
                startActivity(intent)
            }

            R.id.share -> {

                PermissionX.request(this,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE) { allGranted, deniedList ->
                    run {
                        if (allGranted) {

                            val view = window.decorView

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                //获取layout的位置
                                val location = IntArray(2)
                                view.getLocationInWindow(location)
                                //准备一个bitmap对象，用来将copy出来的区域绘制到此对象中
                                val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888, true)
                                PixelCopy.request(this.window,
                                        Rect(location[0], location[1], location[0] + view.width, location[1] + view.height),
                                        bitmap, { copyResult ->
                                    //如果成功
                                    if (copyResult == PixelCopy.SUCCESS) {
                                        cutScreenShotToShare(bitmap)
                                    }else{
                                        this.toast("截图出现错误")
                                    }
                                }, Handler(Looper.getMainLooper()))

                            }else{
                                takeScreenShotToShare()
                            }



                        } else {
                            this.toast("你拒绝了 $deniedList")
                        }
                    }
                }

            }

            R.id.lock_screen -> {

                PermissionX.request(this,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE) { allGranted, deniedList ->
                    run {
                        if (allGranted) {

                            val view = window.decorView

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                //获取layout的位置
                                val location = IntArray(2)
                                view.getLocationInWindow(location)
                                //准备一个bitmap对象，用来将copy出来的区域绘制到此对象中
                                val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888, true)
                                PixelCopy.request(this.window,
                                        Rect(location[0], location[1], location[0] + view.width, location[1] + view.height),
                                        bitmap, { copyResult ->
                                    //如果成功
                                    if (copyResult == PixelCopy.SUCCESS) {
                                        cutScreenShotToLock(bitmap)
                                    }else{
                                        this.toast("截图出现错误")
                                    }
                                }, Handler(Looper.getMainLooper()))


                            }else{
                                takeScreenShotToLock()
                            }



                        } else {
                            this.toast("你拒绝了 $deniedList")
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
        PictureUtil().savePictureToPhotoAlbum(this, screenShot)

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

        //去掉状态栏和标题栏和虚拟导航栏

        return bitmap.let { Bitmap.createBitmap(it, 0, toolbarHeight + statusbarHeight, width, height - toolbarHeight - statusbarHeight - getVirtualBarHeight()) }
    }

    //获取虚拟导航栏的高度
    private fun getVirtualBarHeight():Int{
        var height = 0
        val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display
        } else {
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay
        }
        val dm = DisplayMetrics()
        try {
            val c = Class.forName("android.view.Display")
            val method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
            method.invoke(display, dm)
            val displayMetrics = resources.displayMetrics
            height = dm.heightPixels - displayMetrics.heightPixels
        } catch (e:Exception) {
            e.printStackTrace()
        }
        return height
    }

}


