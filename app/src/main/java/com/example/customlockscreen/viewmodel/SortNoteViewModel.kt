package com.example.customlockscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.customlockscreen.model.bean.SortNote

class SortNoteViewModel: ViewModel() {
    var sortNote : MutableLiveData<SortNote> = MutableLiveData()
}