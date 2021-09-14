package com.example.customlockscreen.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.customlockscreen.model.bean.SortNote

@Dao
interface SortNoteDao {

    @Insert
    fun insertSortNote(sortNote: SortNote)

    @Delete
    fun deleteSortNote(sortNote: SortNote)

    @Query("DELETE FROM sort_note_table")
    fun deleteAllSortNote()

    @Update
    fun updateSortNote(sortNote: SortNote)

    @Query("SELECT * FROM sort_note_table")
    fun getAllSortNotes():List<SortNote>

    @Query("SELECT * FROM sort_note_table")
    fun getAllSortNotesByObserve():LiveData<List<SortNote>>


    @Query("SELECT name FROM sort_note_table")
    fun getAllSortNotesName():List<String>

    @Query("SELECT COUNT(*) FROM sort_note_table")
    fun getSortNoteCount():Int

}