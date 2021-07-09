package com.example.customlockscreen.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import com.example.customlockscreen.R
import com.example.customlockscreen.Util.SharePref
import com.example.customlockscreen.activity.BackupDataActivity
import com.example.customlockscreen.activity.LockScreenSettingActivity
import com.example.customlockscreen.activity.TimeRemindActivity
import com.example.customlockscreen.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    private lateinit var binding:FragmentSettingBinding

    private var sortStyle :String ?= null

    private lateinit var sharedPreferences : SharedPreferences

    private lateinit var edit : SharedPreferences.Editor


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentSettingBinding.inflate(layoutInflater)


        sharedPreferences = context!!.getSharedPreferences("LABEL_EVENT",Context.MODE_PRIVATE)


        sortStyle = sharedPreferences.getString("sortStyle","按事件时间")

        binding.sortStyle.text = sortStyle




        binding.settingSortLayout.setOnClickListener {
            showMenu(it,R.menu.fragment_setting_sort_style_menu)
        }
        
        binding.settingClockLayout.setOnClickListener {
            val intent = Intent(context,TimeRemindActivity::class.java)
            startActivity(intent)
        }
        
        binding.settingBackupDataLayout.setOnClickListener {
            val intent = Intent(context,BackupDataActivity::class.java)
            startActivity(intent)
        }

        binding.setLockScreenLayout.setOnClickListener {
            val intent = Intent(context,LockScreenSettingActivity::class.java)
            startActivity(intent)
        }



    }





    @RequiresApi(Build.VERSION_CODES.M)
    private fun showMenu(v:View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(context!!,v)
        popup.menuInflater.inflate(menuRes,popup.menu)
        popup.gravity = Gravity.RIGHT

        popup.setOnMenuItemClickListener (object :PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(menuItem: MenuItem?): Boolean {

                when(menuItem?.itemId){
                    R.id.sort_by_add_time->{
                        sortStyle = "按添加时间"
                    }


                    R.id.sort_by_event_time->{
                        sortStyle =  "按事件时间"

                    }


                }

                binding.sortStyle.text = sortStyle

                edit = sharedPreferences.edit()
                edit.putString("sortStyle",sortStyle).apply()

                popup.dismiss()

                return true
            }


        })


        popup.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return binding.root
    }


}





