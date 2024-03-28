package com.p1neapplexpress.telegrec.room

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.p1neapplexpress.telegrec.data.Recording
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordingsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recording: Recording)

    @Delete
    suspend fun delete(recording: Recording)

    @Query("SELECT * FROM recordings")
    fun getAll() : Flow<List<Recording>>
}