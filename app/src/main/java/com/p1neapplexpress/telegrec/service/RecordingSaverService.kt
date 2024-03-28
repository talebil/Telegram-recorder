package com.p1neapplexpress.telegrec.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.p1neapplexpress.telegrec.IRecordingSaver
import com.p1neapplexpress.telegrec.data.Recording
import com.p1neapplexpress.telegrec.preferences.AppSettings
import com.p1neapplexpress.telegrec.repository.RecordingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.io.File

class RecordingSaverService : Service(), KoinComponent {

    private val appSettings: AppSettings = get()
    private val recordingsRepository: RecordingsRepository = get()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val binder = object : IRecordingSaver.Stub() {
        override fun writeRecording(name: String?, buf: ByteArray?) {
            File(appSettings.savePath + "/" + name).apply {
                appendBytes(buf ?: throw RuntimeException("null buffer"))
            }
        }

        override fun saveRecording(rec: Recording?) {
            scope.launch {
                val recording = rec ?: return@launch
                recordingsRepository.saveRecording(
                    rec.copy(
                        file = File(appSettings.savePath, recording.filename).absolutePath
                    )
                )
            }
        }

        override fun getRecordingFormat(): String = appSettings.recordingFormat.name
        override fun getFileNameMask(): String = appSettings.fileNameMask
    }

    override fun onBind(p0: Intent?): IBinder = binder

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}