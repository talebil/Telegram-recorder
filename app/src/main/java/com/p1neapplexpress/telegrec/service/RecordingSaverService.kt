package com.p1neapplexpress.telegrec.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.p1neapplexpress.telegrec.IRecordingSaver
import com.p1neapplexpress.telegrec.preferences.AppSettings
import com.p1neapplexpress.telegrec.util.logd
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.io.File

class RecordingSaverService : Service(), KoinComponent {

    private val appSettings: AppSettings = get()

    private val binder = object : IRecordingSaver.Stub() {
        override fun saveRecording(name: String?, buf: ByteArray?) {
            File(appSettings.savePath + "/" + name).apply {
                appendBytes(buf ?: throw RuntimeException("null buffer"))
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder = binder
}