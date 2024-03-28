package com.p1neapplexpress.telegrec.di

import android.content.Context
import androidx.room.Room
import com.p1neapplexpress.telegrec.repository.RecordingsRepository
import com.p1neapplexpress.telegrec.room.RecordingsDAO
import com.p1neapplexpress.telegrec.room.RecordingsDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


fun provideDatabase(context: Context) = Room
    .databaseBuilder(context, RecordingsDatabase::class.java, "RECORDINGS_DATABASE")
    .fallbackToDestructiveMigration()
    .build()

fun provideRecordingsDAO(db: RecordingsDatabase): RecordingsDAO = db.recordingsDAO()

val databaseModule = module {
    single { provideDatabase(androidContext()) }
    single { provideRecordingsDAO(get()) }
    factory { RecordingsRepository(get()) }
}