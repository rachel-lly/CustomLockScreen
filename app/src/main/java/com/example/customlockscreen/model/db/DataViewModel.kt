package com.example.customlockscreen.model.db

import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {

    private val dataBase  = DataBase.dataBase

    private val labelDao = dataBase.labelDao()
    private val sortNoteDao = dataBase.sortNoteDao()


    fun getAllLabelsByObserve() = labelDao.getAllLabelsByObserve()
}