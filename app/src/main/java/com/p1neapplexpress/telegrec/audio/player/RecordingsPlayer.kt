package com.p1neapplexpress.telegrec.audio.player

import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Handler
import com.p1neapplexpress.telegrec.data.Recording

class RecordingsPlayer(private val mediaPlayer: MediaPlayer) : OnPreparedListener {
    private var listener: PlaybackStateUpdatedListener? = null
    private var currentRecording: Recording? = null
    private val handler = Handler()
    private val updatePositionRunnable: Runnable = object : Runnable {
        override fun run() {
            if (mediaPlayer.isPlaying) {
                val currentPosition = mediaPlayer.currentPosition
                val duration = mediaPlayer.duration

                listener?.onUpdate(
                    PlaybackState(
                        recording = currentRecording ?: return,
                        currentPosition = currentPosition.toLong(),
                        duration = duration.toLong()
                    )
                )
            }

            handler.postDelayed(this, 1000)
        }
    }

    fun setPlaybackStateUpdatedListener(appSettingsUpdatedListener: PlaybackStateUpdatedListener) {
        this.listener = appSettingsUpdatedListener
    }

    fun removePlaybackStateUpdatedListener() {
        this.listener = null
    }

    fun select(recording: Recording) {
        try {
            if (recording == currentRecording) {
                togglePlayPause()
            } else {
                play(recording)
            }
        } catch (e: Exception) {
            listener?.onError(e)
        }
    }

    fun togglePlayPause() {
        if (mediaPlayer.isPlaying) mediaPlayer.pause() else mediaPlayer.start()
    }

    fun play(recording: Recording) {
        if (mediaPlayer.isPlaying) stop()
        mediaPlayer.setDataSource(recording.file)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener(this)
        handler.post(updatePositionRunnable)
        currentRecording = recording
    }

    fun stop() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.setOnPreparedListener(null)
        handler.removeCallbacks(updatePositionRunnable)
        currentRecording = null
    }

    override fun onPrepared(p0: MediaPlayer?) {
        p0?.start()
    }
}