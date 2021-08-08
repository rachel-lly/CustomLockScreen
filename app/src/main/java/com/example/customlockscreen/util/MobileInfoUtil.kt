package com.example.customlockscreen.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_SETTINGS
import android.util.Log

class MobileInfoUtil {

    companion object{
        private fun getMobileType() = Build.MANUFACTURER

        //跳转设置的开机自启动页面
        fun jumpStartInterfece(context: Context){

            var intent = Intent()
            try {

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.e("HLQ_Struggle", "******************当前手机型号为：" + getMobileType())
                var componentName: ComponentName? = null
                if (getMobileType().equals("Xiaomi")) { // 红米Note4测试通过
                    componentName = ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
                } else if (getMobileType().equals("Letv")) { // 乐视2测试通过
                    intent.setAction("com.letv.android.permissionautoboot")
                } else if (getMobileType().equals("samsung")) { // 三星Note5测试通过
                    componentName = ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.ram.AutoRunActivity")
                } else if (getMobileType().equals("HUAWEI")) { // 华为测试通过
                    componentName = ComponentName.unflattenFromString("com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity")!!//跳自启动管理
                    //SettingOverlayView.show(context);
                } else if (getMobileType().equals("vivo")) { // VIVO测试通过
                    componentName = ComponentName.unflattenFromString("com.iqoo.secure/.safeguard.PurviewTabActivity")!!
                } else if (getMobileType().equals("Meizu")) { //万恶的魅族
                    // 通过测试，发现魅族是真恶心，也是够了，之前版本还能查看到关于设置自启动这一界面，系统更新之后，完全找不到了，心里默默Fuck！
                    // 针对魅族，我们只能通过魅族内置手机管家去设置自启动，所以我在这里直接跳转到魅族内置手机管家界面，具体结果请看图
                    componentName = ComponentName.unflattenFromString("com.meizu.safe/.permission.PermissionMainActivity")!!
                } else if (getMobileType().equals("OPPO")) { // OPPO R8205测试通过
                    componentName = ComponentName.unflattenFromString("com.oppo.safe/.permission.startup.StartupAppListActivity")!!
                } else if (getMobileType().equals("ulong")) { // 360手机 未测试
                    componentName = ComponentName("com.yulong.android.coolsafe", ".ui.activity.autorun.AutoRunListActivity")
                } else {
                    // 以上只是市面上主流机型，由于公司你懂的，所以很不容易才凑齐以上设备
                    // 针对于其他设备，我们只能调整当前系统app查看详情界面
                    // 在此根据用户手机当前版本跳转系统设置界面
                    intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                    intent.data = Uri.fromParts("package", context.packageName, null)
                }
                intent.component = componentName
                context.startActivity(intent)

            }catch (e: Exception) {//抛出异常就直接打开设置页面
                intent = Intent(ACTION_SETTINGS)
                context.startActivity(intent)
            }

        }
    }





}