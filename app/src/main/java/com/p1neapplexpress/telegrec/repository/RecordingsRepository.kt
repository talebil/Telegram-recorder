package com.p1neapplexpress.telegrec.repository

import com.p1neapplexpress.telegrec.data.Recording
import com.p1neapplexpress.telegrec.room.RecordingsDAO

class RecordingsRepository(private val dao: RecordingsDAO) {
    suspend fun saveRecording(recording: Recording) = dao.insert(recording)
    suspend fun deleteRecording(recording: Recording) = dao.delete(recording)
    fun getAllRecordings() = dao.getAll()
}