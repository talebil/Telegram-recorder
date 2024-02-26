package com.p1neapplexpress.telegrec.data

data class Recording(
    val app: String,
    val callerID: String,
    val fileName: String,
    val date: Long
)
