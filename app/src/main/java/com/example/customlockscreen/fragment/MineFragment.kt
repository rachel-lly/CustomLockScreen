package com.example.customlockscreen.fragment

import android.app.Activity.RESULT_OK
import android.app.ActivityOptions
import android.content.Intent
import android.database.Cursor
import android.graphics.*
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.customlockscreen.activity.ChangeAvatarActivity
import com.example.customlockscreen.application.MyApplication
import com.example.customlockscreen.databinding.FragmentMineBinding
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.model.db.DataViewModel
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
            val intent = Intent(activity,ChangeAvatarActivity::class.java)
            val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity,binding.mineAvater,"avatar")
            startActivity(intent,transitionActivityOptions.toBundle())

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return binding.root
    }
}