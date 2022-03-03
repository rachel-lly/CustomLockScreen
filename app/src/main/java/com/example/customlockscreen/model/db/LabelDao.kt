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

    @Query("DELETE FROM label_table")
    fun deleteAllLabel()

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

    @Query("SELECT COUNT(*) FROM label_table")
    fun getLabelCount():Int

    @Query("SELECT COUNT(*) FROM label_table WHERE sortNote =:sortNoteName")
    fun getLabelCountBySameSort(sortNoteName: String):Int

    @Query("UPDATE label_table SET text =:name WHERE id =:id")
    fun updateLabelByName(name: String,id: Int)

    @Query("UPDATE label_table SET sortNote =:sortNote WHERE id =:id")
    fun updateLabelBySortNote(sortNote: String,id: Int)

    @Query("SELECT * FROM label_table WHERE id =:id")
    fun getLabel(id:Int):LiveData<Label>

    @Query("SELECT * FROM label_table WHERE id =:id")
    fun getLabelById(id:Int):Label
}