package com.example.customlockscreen.model.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LABEL_TABLE")
data class Label(
        @PrimaryKey var text : String,
        @ColumnInfo var targetDate: Long,
        @ColumnInfo var addNoteTime:Long
        ) : Comparable<Any> {

        @ColumnInfo var day = (targetDate-System.currentTimeMillis())/(1000*3600*24)

        @ColumnInfo var sortNote :String = "生活"

        @ColumnInfo var isTop :Boolean = false

        @ColumnInfo var isLockScreen = false

        @ColumnInfo var isEnd = false

        @ColumnInfo var endDate:Long = 1000000L


        override fun compareTo(other: Any): Int {
              if(this.day<(other as Label).day){
                      return 1
              }else{
                      return -1
              }
        }
}


