package com.p1neapplexpress.telegrec.xposed.recorder

import com.p1neapplexpress.telegrec.audio.AudioCaptureThread
import com.p1neapplexpress.telegrec.audio.AudioChannel
import com.p1neapplexpress.telegrec.audio.AudioProcessor
import com.p1neapplexpress.telegrec.data.RecordingFormat
import com.p1neapplexpress.telegrec.preferences.AppSettings
import com.p1neapplexpress.telegrec.su.CP
import com.p1neapplexpress.telegrec.su.MakeDirectories
import com.p1neapplexpress.telegrec.util.formatFileName
import com.p1neapplexpress.telegrec.util.loge
import com.p1neapplexpress.telegrec.util.postDelayed
import com.p1neapplexpress.telegrec.xposed.AudioCaptureHook
import com.p1neapplexpress.telegrec.xposed.XposedTelegramRecorder.Companion.getXPref
import com.p1neapplexpress.telegrec.xposed.getTemporaryFile
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.File

abstract class AbstractRecorder(
    private val packageName: List<String>,
    private val param: XC_LoadPackage.LoadPackageParam
) {
    open var callerID: String? = null

    private var appSettings: AppSettings? = null
    private val audioProcessor = AudioProcessor()
    private val hook = AudioCaptureHook(param)
    private var threads: List<AudioCaptureThread>? = null

    fun run() {
        if (packageName.contains(param.packageName)) {
            getXPref()?.let {
                appSettings = AppSettings(it)
            }

            hook.hookAudioCapture()
            hook()
        }
    }

    fun beginCapture() {
        threads = listOf(
            AudioCaptureThread(getTemporaryFile(param.packageName, AudioChannel.AUDIO_UPLINK)),
            AudioCaptureThread(getTemporaryFile(param.packageName, AudioChannel.AUDIO_DOWNLINK))
        )

        threads!!.map { it.start() }

        hook.onNewRecordData = { threads!![0].bufferStack.push(it) }
        hook.onNewTrackData = { threads!![1].bufferStack.push(it) }
    }

    fun endCapture() {
        val recordingParts = mutableListOf<File>()

        threads?.map { it.interrupt(); recordingParts.add(it.dst) }
        threads = null

        // Wait a little while wave file header being updated
        // then merge our recording parts
        postDelayed {
            if (recordingParts.size < 2) {
                loge("Failure :( not enough parts to merge")
                return@postDelayed
            }

            val recordingFormat = appSettings?.recordingFormat ?: RecordingFormat.MP3
            val tempFile = getTemporaryFile(param.packageName, format = recordingFormat).apply { delete() }
            audioProcessor.mergeAndConvert(
                recordingParts[0],
                recordingParts[1],
                tempFile,
                onComplete = {
                    recordingParts.map { it.delete() }
                    processingComplete(tempFile, recordingFormat)
                },
                onError = { loge(it) }
            )
        }
    }

    private fun processingComplete(result: File, recordingFormat: RecordingFormat) {
        val formatString = ".${recordingFormat.name.lowercase()}"
        val fileName = formatFileName(appSettings!!.fileNameMask, callerID) + formatString

        var savePath = appSettings!!.savePath
        if (!savePath.endsWith("/")) savePath += "/"

        MakeDirectories(savePath).exec()
        CP(result.absolutePath, "$savePath$fileName").execAsync()
    }

    abstract fun hook()
}