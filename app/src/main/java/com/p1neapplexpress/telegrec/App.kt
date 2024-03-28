package com.p1neapplexpress.telegrec

import android.annotation.SuppressLint
import android.app.Application
import com.p1neapplexpress.telegrec.di.appSettingsModule
import com.p1neapplexpress.telegrec.di.databaseModule
import com.p1neapplexpress.telegrec.di.recordingsPlayerModule
import com.p1neapplexpress.telegrec.preferences.AppSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

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