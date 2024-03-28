package com.p1neapplexpress.telegrec.xposed

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.p1neapplexpress.telegrec.BuildConfig
import com.p1neapplexpress.telegrec.IRecordingSaver
import com.p1neapplexpress.telegrec.data.Recording
import com.p1neapplexpress.telegrec.data.RecordingFormat
import com.p1neapplexpress.telegrec.util.asClass
import com.p1neapplexpress.telegrec.util.doAfter
import com.p1neapplexpress.telegrec.util.logd
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class RecordingSaverHook(param: XC_LoadPackage.LoadPackageParam) {

    private var recordingSaver: IRecordingSaver? = null
    private val classLoader: ClassLoader = param.classLoader

    private fun getConnection() = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            recordingSaver = IRecordingSaver.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            recordingSaver = null
        }
    }

    fun hookRecordingSaverBinder() {
        logd("hookRecordingSaverBinder()")
        CONTEXT_WRAPPER_CLASS.asClass(classLoader)?.doAfter(ATTACH_BASE_CONTEXT, Context::class.java) { methodHook ->
            val ctx = methodHook.thisObject as? Application ?: return@doAfter

            val intent = Intent().apply {
                setClassName(BuildConfig.APPLICATION_ID, "${BuildConfig.APPLICATION_ID}$RECORDING_SAVER_SERVICE")
            }

            ctx.bindService(intent, getConnection(), Context.BIND_AUTO_CREATE)
        }
    }

    fun save(recording: Recording) {
        val destFile = File(recording.file)
        with(recordingSaver ?: return) {
            var inputStream: InputStream? = null
            try {
                inputStream = FileInputStream(destFile)
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                while (inputStream.read(buffer) > 0) {
                    writeRecording(destFile.name, buffer)
                }
                saveRecording(recording)
            } finally {
                inputStream?.close()
                destFile.delete()
            }
        }
    }

    fun getRecordingFormat(): RecordingFormat? = recordingSaver?.let { RecordingFormat.valueOf(it.recordingFormat) }
    fun getFileNameMask(): String? = recordingSaver?.fileNameMask

    companion object {
        private const val CONTEXT_WRAPPER_CLASS = "android.content.ContextWrapper"
        private const val ATTACH_BASE_CONTEXT = "attachBaseContext"

        private const val RECORDING_SAVER_SERVICE = ".service.RecordingSaverService"

        private const val DEFAULT_BUFFER_SIZE = 1024
    }

}