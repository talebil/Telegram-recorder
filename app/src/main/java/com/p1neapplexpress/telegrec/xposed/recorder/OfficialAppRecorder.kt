package com.p1neapplexpress.telegrec.xposed.recorder

import com.p1neapplexpress.telegrec.util.asClass
import com.p1neapplexpress.telegrec.util.doAfterAll
import com.p1neapplexpress.telegrec.util.logd
import de.robv.android.xposed.callbacks.XC_LoadPackage

open class OfficialAppRecorder(param: XC_LoadPackage.LoadPackageParam) : AbstractRecorder(param) {

    override val packageNames: List<String> = listOf(OFFICIAL_CLIENT, OFFICIAL_CLIENT_WEB)
    private val classLoader: ClassLoader = param.classLoader

    override fun hook() {
        logd("handleLoadPackage()")

        // Ongoing call started
        VOIP_HELPER_CLASS.asClass(classLoader)?.doAfterAll(START_CALL) {
            beginCapture()
        }

        VOIP_SERVICE_CLASS.asClass(classLoader)?.apply {
            // Incoming call accepted
            doAfterAll(ACCEPT_INCOMING_CALL) {
                beginCapture()
            }

            // Caller ID extraction
            doAfterAll(SHOW_NOTIFICATION) { methodHook ->
                if (methodHook.args.isNotEmpty()) {
                    callerID = methodHook.args[0].toString()
                }
            }

            // Call ended, finish recording
            doAfterAll(CALL_ENDED) {
                endCapture()
            }
        }
    }

    companion object {
        private const val VOIP_HELPER_CLASS = "org.telegram.ui.Components.voip.VoIPHelper"
        private const val START_CALL = "startCall"

        private const val VOIP_SERVICE_CLASS = "org.telegram.messenger.voip.VoIPService"
        private const val ACCEPT_INCOMING_CALL = "acceptIncomingCall"
        private const val SHOW_NOTIFICATION = "showNotification"
        private const val CALL_ENDED = "callEnded"

        // These package names are related to official telegram messenger app for Android
        private const val OFFICIAL_CLIENT = "org.telegram.messenger"
        private const val OFFICIAL_CLIENT_WEB = "org.telegram.messenger.web"
    }

}