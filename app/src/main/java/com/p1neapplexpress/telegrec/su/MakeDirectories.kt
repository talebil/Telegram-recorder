package com.p1neapplexpress.telegrec.su

class MakeDirectories(dst: String) : AbstractSuCommand() {

    override val commands: List<String> = listOf(
        "mkdir -p \"$dst\""
    )

}