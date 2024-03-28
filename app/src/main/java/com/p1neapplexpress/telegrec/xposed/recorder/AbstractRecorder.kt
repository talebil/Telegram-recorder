package com.p1neapplexpress.telegrec.xposed.recorder

import com.p1neapplexpress.telegrec.audio.AudioCaptureThread
import com.p1neapplexpress.telegrec.audio.AudioChannel
import com.p1neapplexpress.telegrec.audio.AudioProcessor
import com.p1neapplexpress.telegrec.data.Recording
import com.p1neapplexpress.telegrec.data.RecordingFormat
import com.p1neapplexpress.telegrec.util.formatFileName
import com.p1neapplexpress.telegrec.util.logd
import com.p1neapplexpress.telegrec.util.loge
import com.p1neapplexpress.telegrec.util.postDelayed
import com.p1neapplexpress.telegrec.xposed.AudioCaptureHook
import com.p1neapplexpress.telegrec.xposed.RecordingSaverHook
import com.p1neapplexpress.telegrec.xposed.getResultFileDir
import com.p1neapplexpress.telegrec.xposed.getTemporaryFile
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.File

abstract class AbstractRecorder(private val param: XC_LoadPackage.LoadPackageParam) {

    abstract val packageNames: List<String>
    open var callerID: String? = null

    private val audioProcessor = AudioProcessor()
    private val captureHook = AudioCaptureHook(param)
    private val recordingSaverHook = RecordingSaverHook(param)
    private var threads: List<AudioCaptureThread>? = null

    abstract fun hook()

    fun run() {
        if (packageNames.contains(param.packageName)) {
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

        captureHook.onNewRecordData = { data -> threads?.let { it[0].bufferStack.push(data) } }
        captureHook.onNewTrackData = { data -> threads?.let { it[1].bufferStack.push(data) } }
    }

    fun endCapture() {
        val recordingParts = mutableListOf<File>()

        threads?.map { it.interrupt(); recordingParts.add(it.dst) }
        threads = null

        // Wait a little while for the wav file header to be updated
        // then merge your recording parts.
        postDelayed {
            if (recordingParts.size < 2) {
                loge("Failure :( not enough parts to merge")
                return@postDelayed
            }

            val recordingFormat = recordingSaverHook.getRecordingFormat() ?: RecordingFormat.MP3
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
        logd("${recordingSaverHook.getRecordingFormat()}, ${recordingSaverHook.getFileNameMask()}")
        val formatString = ".${recordingFormat.name.lowercase()}"
        val currentDate = System.currentTimeMillis()
        val fileName = formatFileName(recordingSaverHook.getFileNameMask() ?: "recording", callerID, date = currentDate) + formatString
        val resultFileDir = getResultFileDir(param.packageName)
        val destFile = File(resultFileDir, fileName)

        resultFileDir.mkdirs()
        result.copyTo(destFile)
        result.delete()

        recordingSaverHook.save(Recording(
            app = param.packageName,
            callerID = callerID ?: "null",
            file = destFile.absolutePath,
            filename = destFile.name,
            date = currentDate
        ))
    }
}