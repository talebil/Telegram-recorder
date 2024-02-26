package com.p1neapplexpress.telegrec.util

import android.util.Log
import com.p1neapplexpress.telegrec.BuildConfig

fun Any.logd(message: Any) {
    Log.d("${BuildConfig.APPLICATION_ID}:${this.javaClass.simpleName}", message.toString())
}

fun Any.logi(message: Any) {
    Log.i("${BuildConfig.APPLICATION_ID}:${this.javaClass.simpleName}", message.toString())
}

fun Any.loge(error: Throwable) {
    Log.e("${BuildConfig.APPLICATION_ID}:${this.javaClass.simpleName}", error.stackTraceToString())
}

fun Any.loge(error: String) {
    Log.e("${BuildConfig.APPLICATION_ID}:${this.javaClass.simpleName}", error)
}