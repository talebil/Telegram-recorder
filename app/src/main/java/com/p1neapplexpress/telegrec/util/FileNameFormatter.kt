package com.p1neapplexpress.telegrec.util

fun formatFileName(mask: String, callerID: String? = null) = mask
    .replace("%date", PrettyDate.now())
    .replace("%caller_id", callerID ?: "N/A")
