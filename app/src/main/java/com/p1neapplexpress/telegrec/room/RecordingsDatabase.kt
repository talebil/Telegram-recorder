package com.p1neapplexpress.telegrec.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.p1neapplexpress.telegrec.data.Recording

@Database(entities = [Recording::class], version = 2, exportSchema = false)
abstract class RecordingsDatabase : RoomDatabase() {
    abstract fun recordingsDAO(): RecordingsDAO
}