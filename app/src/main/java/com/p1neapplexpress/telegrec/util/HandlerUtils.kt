package com.p1neapplexpress.telegrec.util

import android.os.Handler
import android.os.Looper

fun postDelayed(delay: Long = 1000, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(action, delay)
}