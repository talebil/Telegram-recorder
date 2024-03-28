package com.p1neapplexpress.telegrec

import android.app.Application
import com.p1neapplexpress.telegrec.di.appSettingsModule
import com.p1neapplexpress.telegrec.di.databaseModule
import com.p1neapplexpress.telegrec.di.recordingsPlayerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(databaseModule)
            modules(appSettingsModule)
            modules(recordingsPlayerModule)
        }
    }

}