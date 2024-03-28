package com.p1neapplexpress.telegrec.di

import android.media.MediaPlayer
import com.p1neapplexpress.telegrec.audio.player.RecordingsPlayer
import org.koin.dsl.module

fun provideMediaPlayer() = MediaPlayer()
fun provideRecordingsPlayer(mediaPlayer: MediaPlayer) = RecordingsPlayer(mediaPlayer)

val recordingsPlayerModule = module {
    single { provideMediaPlayer() }
    single { provideRecordingsPlayer(get()) }
}