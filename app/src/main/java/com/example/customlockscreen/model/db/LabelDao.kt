package com.example.customlockscreen.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.customlockscreen.model.bean.Label

@Dao
interface LabelDao {

    @Insert
    fun insertLabel(label: Label)

    @Delete
    fun deleteLabel(label: Label)

    @Update
    fun updateLabel(label: Label)

    @Query("SELECT * FROM label_table")
    fun getAllLabelsByObserve(): LiveData<List<Label>>

    @Query("SELECT * FROM label_table")
    fun getAllLabels(): List<Label>

    @Query("SELECT text FROM label_table")
    fun getAllLabelsName():List<String>

    @Query("SELECT * FROM label_table WHERE sortNote =:sortNoteName ")
    fun getSameSortNoteLabelList(sortNoteName:String):List<Label>

    @Query("SELECT * FROM label_table WHERE text =:text")
    fun getLabelByName(text:String):Label?

}