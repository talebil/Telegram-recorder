package com.p1neapplexpress.telegrec.audio.player

import com.p1neapplexpress.telegrec.data.Recording

data class PlaybackState(
    val recording: Recording?,
    val currentPosition: Long,
    val duration: Long
) {
    companion object {
        val EMPTY = PlaybackState(null, 0, 0)
    }
}