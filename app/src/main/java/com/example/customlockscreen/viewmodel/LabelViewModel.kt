package com.example.customlockscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.customlockscreen.model.bean.Label

class LabelViewModel: ViewModel() {
    var label = MutableLiveData<Label>()


}