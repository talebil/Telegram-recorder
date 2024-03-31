package com.p1neapplexpress.telegrec.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p1neapplexpress.telegrec.audio.player.PlaybackState
import com.p1neapplexpress.telegrec.audio.player.PlaybackStateUpdatedListener
import com.p1neapplexpress.telegrec.audio.player.RecordingsPlayer
import com.p1neapplexpress.telegrec.data.Recording
import com.p1neapplexpress.telegrec.repository.RecordingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.io.FileNotFoundException

class RecordingsViewModel : ViewModel(), KoinComponent, PlaybackStateUpdatedListener {
    private val recordingsRepository: RecordingsRepository = get()
    private val recordingsPlayer: RecordingsPlayer = get()

    private val _recordingsState = MutableStateFlow(listOf<Recording>())
    val recordingsState = _recordingsState.asStateFlow()

    private val _playbackState = MutableStateFlow(PlaybackState.EMPTY)
    val playbackState = _playbackState.asStateFlow()

    private var currentRecording: Recording? = null

    init {
        recordingsPlayer.setPlaybackStateUpdatedListener(this)
        getAllRecordings()
    }

    override fun onUpdate(playbackState: PlaybackState) {
        _playbackState.value = playbackState
    }

    override fun onError(throwable: Throwable) {
        if (throwable is FileNotFoundException) {
            deleteRecording(recording = currentRecording ?: return)
        }
    }

    override fun onCleared() {
        recordingsPlayer.removePlaybackStateUpdatedListener()
        super.onCleared()
    }

    fun playRecording(recording: Recording) {
        currentRecording = recording
        recordingsPlayer.select(recording)
    }

    fun stop() {
        recordingsPlayer.stop()
    }

    private fun getAllRecordings() = viewModelScope.launch {
        recordingsRepository.getAllRecordings().collectLatest {
            _recordingsState.value = it
        }
    }

    private fun deleteRecording(recording: Recording) = viewModelScope.launch {
        recordingsRepository.deleteRecording(recording)
    }
}