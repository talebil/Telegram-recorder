package com.p1neapplexpress.telegrec.xposed

import com.p1neapplexpress.telegrec.BuildConfig
import com.p1neapplexpress.telegrec.xposed.recorder.OfficialAppRecorder
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XSharedPreferences
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
        fun getXPref(): XSharedPreferences? {
            val pref = XSharedPreferences(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID)
            return if (pref.file.canRead()) pref else null
        }

        private val CLIENTS = listOf(
            OfficialAppRecorder::class.java
        )
    }
}