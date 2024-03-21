package com.p1neapplexpress.telegrec.xposed

import android.annotation.SuppressLint
import com.p1neapplexpress.telegrec.audio.AudioChannel
import com.p1neapplexpress.telegrec.data.RecordingFormat
import java.io.File

private const val RECORDING_PARTS_DIR = "recording_parts"
private const val RECORDINGS_DIR = "recordings"

@SuppressLint("SdCardPath")
fun getResultFileDir(packageName: String) = File("/data/data/$packageName/cache/$RECORDINGS_DIR")

@SuppressLint("SdCardPath")
fun getTemporaryFile(packageName: String, channel: AudioChannel? = null, format: RecordingFormat = RecordingFormat.WAV) =
    File("/data/data/$packageName/cache/$RECORDING_PARTS_DIR/${channel?.name ?: "RECORDING.${format.name.lowercase()}"}")
