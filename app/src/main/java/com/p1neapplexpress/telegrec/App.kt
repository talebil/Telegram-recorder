package com.p1neapplexpress.telegrec

import android.annotation.SuppressLint
import android.app.Application
import com.p1neapplexpress.telegrec.preferences.AppSettings
import org.koin.core.context.startKoin
import org.koin.dsl.module

@Suppress("DEPRECATION")
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    @SuppressLint("WorldWriteableFiles", "WorldReadableFiles")
    private fun initKoin() {
        startKoin {
            modules(listOf(
                module {
                    with(applicationContext) {
                        single { this }
                        single { getSharedPreferences(packageName, MODE_WORLD_READABLE) }
                        single { AppSettings(get()) }
                    }
                }
            ))
        }
    }

}