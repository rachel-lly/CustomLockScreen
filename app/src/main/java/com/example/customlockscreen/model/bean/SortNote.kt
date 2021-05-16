package com.example.customlockscreen.model.bean

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SORT_NOTE_TABLE")
data class SortNote(

        @PrimaryKey var name: String,
        @ColumnInfo var iconName: String

        ):Parcelable{
    //var iconName:String = resources.getResourceEntryName(R.mipmap.anniversary)
    //获取的时候
    //var iconId:Int = context.resources.getIdentifier(sortNote.iconName,"mipmap",context.packageName)

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(iconName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SortNote> {
        override fun createFromParcel(parcel: Parcel): SortNote {
            return SortNote(parcel)
        }

        override fun newArray(size: Int): Array<SortNote?> {
            return arrayOfNulls(size)
        }
    }

}


