package com.p1neapplexpress.telegrec.xposed

import android.annotation.SuppressLint
import com.p1neapplexpress.telegrec.audio.AudioChannel
import com.p1neapplexpress.telegrec.data.RecordingFormat
import java.io.File

private const val RECORDING_PART_DIR = "recording_parts"

@SuppressLint("SdCardPath")
fun getTemporaryFile(packageName: String, channel: AudioChannel? = null, format: RecordingFormat = RecordingFormat.WAV) =
    File("/data/data/$packageName/cache/$RECORDING_PART_DIR/${channel?.name ?: "RECORDING.${format.name.lowercase()}"}")
