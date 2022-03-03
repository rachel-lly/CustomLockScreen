package com.example.customlockscreen.util

import com.example.customlockscreen.R

class Code {

    companion object {
        const val LABEL = "LABEL"
        const val LABEL_IS_LOCK = "LABEL_IS_LOCK"
        const val LABEL_TEXT = "LABEL_TEXT"
        const val SORT_NOTE_TEXT = "SORT_NOTE_TEXT"
        const val SORT_NOTE = "SORT_NOTE"

        const val RESULT_CODE = 0
        const val EVENT_SCREENSHOT_SHARE = 22 //截图分享
        const val EVENT_SCREENSHOT_LOCK = 23 //截图设为锁屏
        const val REQUEST_CODE_CAPTURE_CROP = 11111
        const val IMAGE_REQUEST_CODE = 12


        val iconList = intArrayOf(
            R.mipmap.cat,
            R.mipmap.owl, R.mipmap.flamingo,
            R.mipmap.cactus, R.mipmap.marigold,
            R.mipmap.umbrella,
            R.mipmap.happy, R.mipmap.rocket,
            R.mipmap.yellow_star, R.mipmap.love_heart, R.mipmap.earth,
            R.mipmap.music,
            R.mipmap.computer, R.mipmap.cake, R.mipmap.diamond, R.mipmap.work_color, R.mipmap.life_color, R.mipmap.anniverity_color )
    }

}