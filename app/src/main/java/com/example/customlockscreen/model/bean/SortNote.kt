package com.example.customlockscreen.model.bean

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SORT_NOTE_TABLE")
data class SortNote(
        @ColumnInfo var name: String,
        @ColumnInfo var iconName: String
        ):Parcelable{
    //var iconName:String = resources.getResourceEntryName(R.mipmap.anniversary)
    //获取的时候
    //var iconId:Int = context.resources.getIdentifier(sortNote.iconName,"mipmap",context.packageName)

    @PrimaryKey(autoGenerate = true)
    var id:Int = 0

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!
    ) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(iconName)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "SortNote(name='$name', iconName='$iconName', id=$id)"
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


