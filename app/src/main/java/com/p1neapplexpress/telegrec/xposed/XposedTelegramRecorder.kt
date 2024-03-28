package com.p1neapplexpress.telegrec.xposed

import com.p1neapplexpress.telegrec.xposed.recorder.OfficialAppRecorder
import com.p1neapplexpress.telegrec.xposed.recorder.TPlusAppRecorder
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage


class XposedTelegramRecorder : IXposedHookLoadPackage, IXposedHookZygoteInit {

    override fun handleLoadPackage(p0: XC_LoadPackage.LoadPackageParam?) {
        if (p0 == null) return

        CLIENTS.map {
            it.getDeclaredConstructor(XC_LoadPackage.LoadPackageParam::class.java).newInstance(p0).apply {
                run()
            }
        }
    }

    override fun initZygote(p0: IXposedHookZygoteInit.StartupParam?) {}

    companion object {
        private val CLIENTS = listOf(
            OfficialAppRecorder::class.java,
            TPlusAppRecorder::class.java
        )
    }
}