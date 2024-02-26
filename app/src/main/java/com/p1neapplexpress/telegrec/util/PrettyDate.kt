package com.p1neapplexpress.telegrec.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PrettyDate {

    fun now(): String {
        val sdf = SimpleDateFormat("HH_mm_ss_dd_MM_yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

}