package com.p1neapplexpress.telegrec.util

fun formatFileName(mask: String, callerID: String? = null, date: Long = System.currentTimeMillis()) = mask
    .replace("%date", PrettyDate.from(date))
    .replace("%caller_id", callerID ?: "N/A")
