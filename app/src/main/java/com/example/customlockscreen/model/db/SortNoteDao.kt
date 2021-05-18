package com.example.customlockscreen.model.db

import androidx.room.*
import com.example.customlockscreen.model.bean.SortNote

@Dao
interface SortNoteDao {

    @Insert
    fun insertSortNote(sortNote: SortNote)

    @Delete
    fun deleteSortNote(sortNote: SortNote)

    @Update
    fun updateSortNote(sortNote: SortNote)

    @Query("SELECT * FROM sort_note_table")
    fun getAllSortNotes():List<SortNote>

    @Query("SELECT name FROM sort_note_table")
    fun getAllSortNotesName():List<String>
}