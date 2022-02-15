package com.example.customlockscreen.view.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.customlockscreen.R
import com.example.customlockscreen.view.activity.ChangeAvatarActivity
import com.example.customlockscreen.application.MyApplication
import com.example.customlockscreen.databinding.FragmentMineBinding
import com.example.customlockscreen.model.db.DataBase
import com.example.customlockscreen.viewmodel.DataViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class MineFragment : Fragment() {

    private var _binding : FragmentMineBinding ?= null
    private val binding get() = _binding!!

    private val labelDao = DataBase.dataBase.labelDao()

    private val sortNoteDao = DataBase.dataBase.sortNoteDao()

    private lateinit var dataViewModel: DataViewModel

    private val format = SimpleDateFormat("MMM  dd,yyyy", Locale.ENGLISH)

    private var todayTime:Long = MaterialDatePicker.todayInUtcMilliseconds()

    private val today = format.format(todayTime)

    companion object {

        private val fragment = MineFragment()

        @JvmStatic
        fun newInstance() = fragment

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.fragment_mine,container,false)



        // TODO: 2022/1/25 头像
//        val avatarPath = "${FileUtil.getAvatarCacheDir(this.context!!)}/avatar.png"
//
//        if(FileUtil.isExistFile(avatarPath)){
//            val file = File(avatarPath)
//            Glide.with(this.context!!).load(file).into(binding.mineAvater)
//        }else{
//            Glide.with(this.context!!).load(R.drawable.avater).into(binding.mineAvater)
//        }

        Glide.with(this.requireContext()).load(R.drawable.avater).into(binding.mineAvater)

        binding.mineTime.text = today

        binding.eventNum.text = labelDao.getAllLabelsName().size.toString()
        binding.sortNoteNum.text = sortNoteDao.getAllSortNotesName().size.toString()

        binding.mineAvater.setOnClickListener {
            val intent = Intent(activity, ChangeAvatarActivity::class.java)
            val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity,binding.mineAvater,"avatar")
            startActivity(intent,transitionActivityOptions.toBundle())

        }

        dataViewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        dataViewModel.getAllSortNotesByObserve().observe(viewLifecycleOwner, {
            binding.sortNoteNum.text = it.size.toString()
        })

        dataViewModel.getAllLabelsByObserve().observe(viewLifecycleOwner, {
            binding.eventNum.text = it.size.toString()
        })

        val firstInstallTime = context?.let { MyApplication._context!!.packageManager.getPackageInfo(it.packageName, 0).firstInstallTime }

        val day = (todayTime- firstInstallTime!!)/(1000*3600*24)

        binding.useDayNum.text = day.toString()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}