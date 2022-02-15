package com.example.customlockscreen.view.activity

import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.ActivityDarkThemeSettingBinding
import com.example.customlockscreen.model.bean.MessageEvent
import com.example.customlockscreen.util.KvCommission
import com.example.customlockscreen.util.ThemeUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DarkThemeSettingActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDarkThemeSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)

        binding =  DataBindingUtil.setContentView(this,R.layout.activity_dark_theme_setting)

        binding.lifecycleOwner = this

        val slide = Slide()
        slide.slideEdge = Gravity.LEFT
        slide.excludeTarget(android.R.id.statusBarBackground, true)
        window.exitTransition = slide


        val slide2 = Slide()
        slide2.slideEdge = Gravity.RIGHT
        slide2.excludeTarget(android.R.id.statusBarBackground, true)
        window.enterTransition = slide2

        binding.darkThemeSettingToolbar.setNavigationIcon(R.mipmap.back)
        binding.darkThemeSettingToolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }

        var isFollowSystem by KvCommission("isFollowSystem",true)
        binding.followSystemSwitch.isChecked = isFollowSystem
        if(isFollowSystem){
            EventBus.getDefault().post(MessageEvent("系统"))
        }else{
            EventBus.getDefault().post(MessageEvent(if(ThemeUtil.getDarkModeStatus(this)) "夜" else "日"))
        }

        binding.followSystemSwitch.setOnClickListener {
            isFollowSystem = binding.followSystemSwitch.isChecked
            if(isFollowSystem){
                EventBus.getDefault().post(MessageEvent("系统"))
            }else{
                binding.darkThemeLayout.visibility = View.VISIBLE
                binding.dayThemeLayout.visibility = View.VISIBLE
                binding.chooseThemeTx.visibility = View.VISIBLE
                EventBus.getDefault().post(MessageEvent(if(ThemeUtil.getDarkModeStatus(this)) "夜" else "日"))
            }
        }

        binding.dayThemeLayout.setOnClickListener {
            EventBus.getDefault().post(MessageEvent("日"))
        }

        binding.darkThemeLayout.setOnClickListener {
            EventBus.getDefault().post(MessageEvent("夜"))
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messageEvent: MessageEvent){
        val msg = messageEvent.msg
        var isFollowSystem by KvCommission("isFollowSystem",true)
        when(msg){

            "夜","日"->{
                var isDarkTheme by KvCommission("isDarkTheme",false)
                isDarkTheme = msg=="夜"
                isFollowSystem = false
                if(isDarkTheme){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.checkDarkTheme.visibility = View.VISIBLE
                    binding.checkDayTheme.visibility = View.GONE
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.checkDayTheme.visibility = View.VISIBLE
                    binding.checkDarkTheme.visibility = View.GONE
                }
            }

            "系统"->{
                isFollowSystem = true
                binding.darkThemeLayout.visibility = View.GONE
                binding.dayThemeLayout.visibility = View.GONE
                binding.chooseThemeTx.visibility = View.GONE
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }
}