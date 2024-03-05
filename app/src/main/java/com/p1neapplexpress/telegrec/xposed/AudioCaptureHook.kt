package com.p1neapplexpress.telegrec.xposed

import com.p1neapplexpress.telegrec.util.asClass
import com.p1neapplexpress.telegrec.util.doAfterAll
import com.p1neapplexpress.telegrec.util.logd
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.nio.ByteBuffer

class AudioCaptureHook(param: XC_LoadPackage.LoadPackageParam) {
    var onNewTrackData: ((ByteBuffer) -> Unit)? = null
    var onNewRecordData: ((ByteBuffer) -> Unit)? = null
    private val classLoader: ClassLoader = param.classLoader

    fun hookAudioCapture() {
        AUDIO_RECORDER_CLASS.asClass(classLoader)?.doAfterAll(READ) { methodHook ->
            if (methodHook.args.size == 3) onNewRecordData?.invoke(methodHook.args[0] as ByteBuffer)
        }

        AUDIO_TRACK_CLASS.asClass(classLoader)?.doAfterAll(WRITE) { methodHook ->
            if (methodHook.args.size == 3) onNewTrackData?.invoke(methodHook.args[0] as ByteBuffer)
        }
    }

    companion object {
        private const val AUDIO_RECORDER_CLASS = "android.media.AudioRecord"
        private const val READ = "read"

        private const val AUDIO_TRACK_CLASS = "android.media.AudioTrack"
        private const val WRITE = "write"
    }

}