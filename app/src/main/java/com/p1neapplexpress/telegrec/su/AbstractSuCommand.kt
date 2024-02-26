package com.p1neapplexpress.telegrec.su

import com.p1neapplexpress.telegrec.util.loge
import java.io.DataOutputStream
import kotlin.concurrent.thread

abstract class AbstractSuCommand {

    open fun exec(): Boolean {
        var returnValue = false
        try {
            if (commands.isNotEmpty()) {
                val suProcess = Runtime.getRuntime().exec("su")
                DataOutputStream(suProcess.outputStream).use { os ->

                    for (currentCommand in commands) {
                        os.writeBytes("$currentCommand\n")
                        os.flush()
                    }

                    os.writeBytes("exit\n")
                    os.flush()
                }

                val suProcessReturned = suProcess.waitFor()
                returnValue = suProcessReturned != 255
            }
        } catch (e: Exception) {
            loge(e)
        }

        return returnValue
    }

    fun execAsync() {
        thread(start = true) { exec() }
    }

    protected abstract val commands: List<String>
}