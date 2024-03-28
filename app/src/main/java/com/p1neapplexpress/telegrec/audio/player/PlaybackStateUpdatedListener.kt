package com.p1neapplexpress.telegrec.audio.player

interface PlaybackStateUpdatedListener {
    fun onUpdate(playbackState: PlaybackState)
    fun onError(throwable: Throwable)
}