package com.p1neapplexpress.telegrec.audio.player

import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Handler
import com.p1neapplexpress.telegrec.data.Recording

class RecordingsPlayer(private val mediaPlayer: MediaPlayer) : OnPreparedListener, MediaPlayer.OnCompletionListener {

    private inner class UpdatePositionRunnable : Runnable {
        override fun run() {
            if (mediaPlayer.isPlaying) {
                updatePlaybackState()
            }

            handler.postDelayed(this, 1000)
        }
    }

    private var listener: PlaybackStateUpdatedListener? = null
    private var currentRecording: Recording? = null
    private val handler = Handler()
    private val updatePositionRunnable = UpdatePositionRunnable()

    override fun onPrepared(p0: MediaPlayer?) {
        p0?.start()
        updatePlaybackState()
    }

    override fun onCompletion(p0: MediaPlayer?) {
        stop()
    }

    fun setPlaybackStateUpdatedListener(appSettingsUpdatedListener: PlaybackStateUpdatedListener) {
        this.listener = appSettingsUpdatedListener
    }

    fun removePlaybackStateUpdatedListener() {
        this.listener = null
    }

    fun select(recording: Recording) {
        try {
            if (recording == currentRecording) togglePlayPause() else play(recording)
        } catch (e: Exception) {
            listener?.onError(e)
        }
    }

    fun play(recording: Recording) {
        stop()

        mediaPlayer.setDataSource(recording.file)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnCompletionListener(this)
        handler.post(updatePositionRunnable)
        currentRecording = recording
    }

    fun stop() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.setOnPreparedListener(null)
        mediaPlayer.setOnCompletionListener(null)
        handler.removeCallbacks(updatePositionRunnable)
        currentRecording = null

        listener?.onUpdate(PlaybackState.EMPTY)
    }

    private fun togglePlayPause() {
        if (mediaPlayer.isPlaying) mediaPlayer.pause() else mediaPlayer.start()
    }

    private fun updatePlaybackState() {
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
}