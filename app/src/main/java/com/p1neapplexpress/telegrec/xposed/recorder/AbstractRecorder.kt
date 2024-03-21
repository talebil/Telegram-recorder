package com.p1neapplexpress.telegrec.xposed.recorder

import com.p1neapplexpress.telegrec.audio.AudioCaptureThread
import com.p1neapplexpress.telegrec.audio.AudioChannel
import com.p1neapplexpress.telegrec.audio.AudioProcessor
import com.p1neapplexpress.telegrec.data.RecordingFormat
import com.p1neapplexpress.telegrec.preferences.AppSettings
import com.p1neapplexpress.telegrec.util.formatFileName
import com.p1neapplexpress.telegrec.util.loge
import com.p1neapplexpress.telegrec.util.postDelayed
import com.p1neapplexpress.telegrec.xposed.AudioCaptureHook
import com.p1neapplexpress.telegrec.xposed.RecordingSaverHook
import com.p1neapplexpress.telegrec.xposed.XposedTelegramRecorder.Companion.getXPref
import com.p1neapplexpress.telegrec.xposed.getResultFileDir
import com.p1neapplexpress.telegrec.xposed.getTemporaryFile
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.File

abstract class AbstractRecorder(private val param: XC_LoadPackage.LoadPackageParam) {

    abstract val packageNames: List<String>
    open var callerID: String? = null

    private var appSettings: AppSettings? = null
    private val audioProcessor = AudioProcessor()
    private val captureHook = AudioCaptureHook(param)
    private val recordingSaverHook = RecordingSaverHook(param)
    private var threads: List<AudioCaptureThread>? = null

    abstract fun hook()

    fun run() {
        if (packageNames.contains(param.packageName)) {
            getXPref()?.let {
                appSettings = AppSettings(it)
            }

            captureHook.hookAudioCapture()
            recordingSaverHook.hookRecordingSaverBinder()
            hook()
        }
    }

    fun beginCapture() {
        threads = listOf(
            AudioCaptureThread(getTemporaryFile(param.packageName, AudioChannel.AUDIO_UPLINK)),
            AudioCaptureThread(getTemporaryFile(param.packageName, AudioChannel.AUDIO_DOWNLINK))
        )

        threads!!.map { it.start() }

        captureHook.onNewRecordData = { threads!![0].bufferStack.push(it) }
        captureHook.onNewTrackData = { threads!![1].bufferStack.push(it) }
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
        val resultFileDir = getResultFileDir(param.packageName)
        val destFile = File(resultFileDir, fileName)

        resultFileDir.mkdirs()
        result.copyTo(destFile)
        result.delete()

        recordingSaverHook.save(destFile)
    }
}