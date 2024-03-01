package com.p1neapplexpress.telegrec.preferences

import android.content.SharedPreferences
import com.p1neapplexpress.telegrec.data.RecordingFormat

class AppSettings(private val prefs: SharedPreferences) : SharedPreferences.OnSharedPreferenceChangeListener {

    var isEnabled: Boolean
        get() = prefs.getBoolean(ENABLED_KEY, true)
        set(value) {
            prefs.edit().putBoolean(ENABLED_KEY, value).apply()
        }

    var savePath: String
        get() = prefs.getString(SAVE_PATH_KEY, SAVE_PATH_DEFAULT)!!
        set(value) {
            prefs.edit().putString(SAVE_PATH_KEY, value).apply()
        }

    var fileNameMask: String
        get() = prefs.getString(FILE_NAME_MASK_KEY, FILE_NAME_MASK_DEFAULT)!!
        set(value) {
            prefs.edit().putString(FILE_NAME_MASK_KEY, value).apply()
        }

    var recordingFormat: RecordingFormat
        get() = RecordingFormat.valueOf(prefs.getString(RECORDING_FORMAT_KEY, RecordingFormat.MP3.name)!!)
        set(value) {
            prefs.edit().putString(RECORDING_FORMAT_KEY, value.name).apply()
        }

    private var listener: AppSettingsUpdatedListener? = null

    override fun onSharedPreferenceChanged(prefs: SharedPreferences?, key: String?) {
        if (prefs == null) return

        when (key) {
            ENABLED_KEY -> listener?.onEnabledStateChanged(prefs.getBoolean(ENABLED_KEY, true))
            SAVE_PATH_KEY -> listener?.onSavePathChanged(prefs.getString(SAVE_PATH_KEY, SAVE_PATH_DEFAULT)!!)
            FILE_NAME_MASK_KEY -> listener?.onFileNameMaskChanged(prefs.getString(FILE_NAME_MASK_KEY, FILE_NAME_MASK_DEFAULT)!!)
            RECORDING_FORMAT_KEY -> listener?.onRecordingFormatChanged(
                RecordingFormat.valueOf(this.prefs.getString(RECORDING_FORMAT_KEY, RecordingFormat.MP3.name)!!)
            )
        }
    }

    fun setSettingsUpdatedListener(appSettingsUpdatedListener: AppSettingsUpdatedListener) {
        this.listener = appSettingsUpdatedListener
        prefs.registerOnSharedPreferenceChangeListener(this)
    }

    fun removeSettingsUpdatedListener() {
        this.listener = null
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    companion object {
        private const val ENABLED_KEY = "enabled"
        private const val RECORDING_FORMAT_KEY = "recording_format"
        private const val FILE_NAME_MASK_KEY = "file_name_mask"
        private const val SAVE_PATH_KEY = "save_path"

        private const val SAVE_PATH_DEFAULT = "/storage/emulated/0/Recordings"
        private const val FILE_NAME_MASK_DEFAULT = "Recording_%date"
    }
}
