package com.p1neapplexpress.telegrec.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.p1neapplexpress.telegrec.preferences.AppSettings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


fun provideSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences(context.packageName, Application.MODE_PRIVATE)
fun provideAppSettings(sharedPreferences: SharedPreferences) = AppSettings(sharedPreferences)

val appSettingsModule = module {
    single { provideSharedPreferences(androidContext()) }
    single { provideAppSettings(get()) }
}