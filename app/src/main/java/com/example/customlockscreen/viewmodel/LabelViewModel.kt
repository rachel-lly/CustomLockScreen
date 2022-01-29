package com.example.customlockscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.util.TimeManager

class LabelViewModel: ViewModel() {
    var label = MutableLiveData<Label>()

    fun getDate(targetDate: Long) = TimeManager.format.format(targetDate)


}