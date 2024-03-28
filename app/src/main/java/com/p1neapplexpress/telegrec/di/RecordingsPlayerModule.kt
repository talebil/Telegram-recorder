package com.p1neapplexpress.telegrec.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.p1neapplexpress.telegrec.audio.player.RecordingsPlayer
import com.p1neapplexpress.telegrec.repository.RecordingsRepository
import com.p1neapplexpress.telegrec.room.RecordingsDAO
import com.p1neapplexpress.telegrec.room.RecordingsDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun provideMediaPlayer() = MediaPlayer()
fun provideRecordingsPlayer(mediaPlayer: MediaPlayer) = RecordingsPlayer(mediaPlayer)

val recordingsPlayerModule = module {
    single { provideMediaPlayer() }
    single { provideRecordingsPlayer(get()) }
}