package com.p1neapplexpress.telegrec

import android.annotation.SuppressLint
import android.app.Application
import com.p1neapplexpress.telegrec.preferences.AppSettings
import com.p1neapplexpress.telegrec.util.logd
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
                        single {
                            try {
                                logd("starting in MODE_WORLD_READABLE mode")
                                getSharedPreferences(packageName, MODE_WORLD_READABLE)
                            } catch (se: SecurityException) {
                                logd("starting in MODE_PRIVATE mode")
                                getSharedPreferences(packageName, MODE_PRIVATE)
                            }
                        }
                    }

                    single { AppSettings(get()) }
                }
            ))
        }
    }

}