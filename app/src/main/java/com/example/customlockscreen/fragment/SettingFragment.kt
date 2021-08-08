package com.example.customlockscreen.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import com.example.customlockscreen.R
import com.example.customlockscreen.util.SharedPreferenceCommission
import com.example.customlockscreen.activity.BackupDataActivity
import com.example.customlockscreen.activity.LockScreenSettingActivity
import com.example.customlockscreen.activity.TimeRemindActivity
import com.example.customlockscreen.databinding.FragmentSettingBinding
import com.example.customlockscreen.model.bean.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class SettingFragment : Fragment() {

    private lateinit var binding:FragmentSettingBinding

    private var sortStyle :String ?= null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentSettingBinding.inflate(layoutInflater)


        val sortStyle by SharedPreferenceCommission(context!!,"sortStyle","按事件时间")

        EventBus.getDefault().post(sortStyle.let { MessageEvent(it) })

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

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem?.itemId) {
                R.id.sort_by_add_time -> {
                    sortStyle = "按添加时间"
                }

                R.id.sort_by_event_time -> {
                    sortStyle = "按事件时间"
                }

            }

            binding.sortStyle.text = sortStyle
            EventBus.getDefault().post(sortStyle?.let { MessageEvent(it) })
            popup.dismiss()

            true
        }

        popup.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        EventBus.getDefault().register(this)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messageEvent:MessageEvent){

        val msg = messageEvent.msg

        when(msg){

            "按添加时间", "按事件时间" ->{
                binding.sortStyle.text = msg
                var style by SharedPreferenceCommission(context!!,"sortStyle","按事件时间")
                style = msg
            }
        }

    }
}





