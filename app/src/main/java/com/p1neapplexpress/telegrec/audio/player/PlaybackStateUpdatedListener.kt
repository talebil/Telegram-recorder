package com.p1neapplexpress.telegrec.audio.player

import com.p1neapplexpress.telegrec.data.Recording

interface PlaybackStateUpdatedListener {
    fun onUpdate(playbackState: PlaybackState)
    fun onError(throwable: Throwable)
}