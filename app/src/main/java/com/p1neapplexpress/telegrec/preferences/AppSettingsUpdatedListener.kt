package com.p1neapplexpress.telegrec.preferences

import com.p1neapplexpress.telegrec.data.RecordingFormat

interface AppSettingsUpdatedListener {
    fun onEnabledStateChanged(state: Boolean)
    fun onRecordingFormatChanged(format: RecordingFormat)
    fun onSavePathChanged(savePath: String)
    fun onFileNameMaskChanged(fileNameMask: String)
}