package com.p1neapplexpress.telegrec.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PrettyDate {

    const val SPACE_PATTERN = "HH:mm:ss dd MM yyyy"
    private const val UNDERSCORE_PATTERN = "HH_mm_ss_dd_MM_yyyy"

    fun now(pattern: String = UNDERSCORE_PATTERN): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date())
    }

    fun from(date: Long, pattern: String = UNDERSCORE_PATTERN): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(date))
    }

}