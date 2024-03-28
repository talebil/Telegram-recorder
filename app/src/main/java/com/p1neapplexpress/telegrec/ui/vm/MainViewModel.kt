package com.p1neapplexpress.telegrec.ui.vm

import androidx.lifecycle.ViewModel
import com.p1neapplexpress.telegrec.data.RecordingFormat
import com.p1neapplexpress.telegrec.preferences.AppSettings
import com.p1neapplexpress.telegrec.preferences.AppSettingsUpdatedListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MainViewModel : ViewModel(), KoinComponent, AppSettingsUpdatedListener {

    private val appSettings: AppSettings = get()

    private val _enabledState = MutableStateFlow(true)
    val enabledState = _enabledState.asStateFlow()

    private val _recordingsFormatState = MutableStateFlow(String())
    val recordingsFormatState = _recordingsFormatState.asStateFlow()

    private val _savePathState = MutableStateFlow(String())
    val savePathState = _savePathState.asStateFlow()

    private val _fileNameMaskState = MutableStateFlow(String())
    val fileNameMaskState = _fileNameMaskState.asStateFlow()

    private val _waveformState = MutableStateFlow(shortArrayOf() to shortArrayOf())
    val waveformState = _waveformState.asStateFlow()


    init {
        appSettings.setSettingsUpdatedListener(this)

        _enabledState.value = appSettings.isEnabled
        _savePathState.value = appSettings.savePath
        _fileNameMaskState.value = appSettings.fileNameMask
        _recordingsFormatState.value = appSettings.recordingFormat.name
    }

    override fun onCleared() {
        appSettings.removeSettingsUpdatedListener()
        super.onCleared()
    }

    override fun onEnabledStateChanged(state: Boolean) {
        _enabledState.value = state
    }

    override fun onRecordingFormatChanged(format: RecordingFormat) {
        _recordingsFormatState.value = format.name
    }

    override fun onSavePathChanged(savePath: String) {
        _savePathState.value = savePath
    }

    override fun onFileNameMaskChanged(fileNameMask: String) {
        _fileNameMaskState.value = fileNameMask
    }

    fun updateEnabledState() {
        appSettings.isEnabled = !appSettings.isEnabled
    }

    fun updateRecordingFormat(format: RecordingFormat) {
        appSettings.recordingFormat = format
    }

    fun updateSavePath(path: String) {
        appSettings.savePath = path
    }

    fun updateFileNameMask(mask: String) {
        appSettings.fileNameMask = mask
    }

}