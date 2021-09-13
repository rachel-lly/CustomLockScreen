package com.example.customlockscreen.fragment

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.graphics.*
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.customlockscreen.R
import com.example.customlockscreen.activity.ChangeAvatarActivity
import com.example.customlockscreen.application.MyApplication
import com.example.customlockscreen.databinding.FragmentMineBinding
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.model.db.DataViewModel
import com.example.customlockscreen.util.ToastUtil.Companion.toast
import com.example.library.PermissionX
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


const val IMAGE_REQUEST_CODE = 12

class MineFragment : Fragment() {


    private lateinit var binding : FragmentMineBinding

    private val labelDao = DataBase.dataBase.labelDao()

    private val sortNoteDao = DataBase.dataBase.sortNoteDao()

    private lateinit var dataViewModel: DataViewModel

    private val format = SimpleDateFormat("MMM  dd,yyyy", Locale.ENGLISH)

    private var todayTime:Long = MaterialDatePicker.todayInUtcMilliseconds()

    private val today = format.format(todayTime)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentMineBinding.inflate(layoutInflater)

        binding.mineTime.text = today

        binding.eventNum.text = labelDao.getAllLabelsName().size.toString()
        binding.sortNoteNum.text = sortNoteDao.getAllSortNotesName().size.toString()

        binding.mineAvater.setOnClickListener {
//            PermissionX.request(activity!!,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE){ allgranted, deniedList ->
//
//                if(allgranted){
//                    //跳转系统相册
//                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                    startActivityForResult(intent, IMAGE_REQUEST_CODE)
//
//                }else{
//                    context!!.toast("你拒绝了 $deniedList")
//                }
//
//            }
            val intent = Intent(context,ChangeAvatarActivity::class.java)
            startActivity(intent)
        }

        dataViewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        dataViewModel.getAllSortNotesByObserve().observe(this, {
            binding.sortNoteNum.text = it.size.toString()
        })

        dataViewModel.getAllLabelsByObserve().observe(this, {
            binding.eventNum.text = it.size.toString()
        })

        val firstInstallTime = context?.let { MyApplication._context!!.packageManager.getPackageInfo(it.packageName, 0).firstInstallTime }

        val day = (todayTime- firstInstallTime!!)/(1000*3600*24)

        binding.useDayNum.text = day.toString()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            IMAGE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    val selectedImageUri = data!!.data

                    val bitmap: Bitmap
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(context!!.contentResolver,selectedImageUri!!)
                        bitmap = ImageDecoder.decodeBitmap(source)
                    }else{
                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor: Cursor = selectedImageUri?.let {
                            context?.contentResolver?.query(it,
                                    filePathColumn, null, null, null)
                        }!! //从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst()
                        val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                        val path = cursor.getString(columnIndex) //获取照片路径

                        cursor.close()
                        bitmap = BitmapFactory.decodeFile(path)
                    }


                    binding.mineAvater.setImageBitmap(bitmap)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return binding.root
    }
}