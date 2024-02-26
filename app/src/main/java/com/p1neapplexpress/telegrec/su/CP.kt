package com.p1neapplexpress.telegrec.su

class CP(src: String, dst: String) : AbstractSuCommand() {

    override val commands: List<String> = listOf(
        "cp \"$src\" \"$dst\""
    )

}