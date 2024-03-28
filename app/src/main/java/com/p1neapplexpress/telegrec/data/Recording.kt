package com.p1neapplexpress.telegrec.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "recordings")
data class Recording(
    @ColumnInfo(name = "app")
    val app: String,
    @ColumnInfo(name = "callerID")
    val callerID: String,
    @ColumnInfo(name = "file")
    val file: String,
    @ColumnInfo(name = "filename")
    val filename: String,
    @ColumnInfo(name = "date")
    val date: Long
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}
