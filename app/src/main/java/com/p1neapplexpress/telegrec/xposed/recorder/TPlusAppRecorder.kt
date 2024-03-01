package com.p1neapplexpress.telegrec.xposed.recorder

import de.robv.android.xposed.callbacks.XC_LoadPackage

class TPlusAppRecorder(param: XC_LoadPackage.LoadPackageParam) : OfficialAppRecorder(param) {

    override val packageNames: List<String> = listOf(PLUS_CLIENT)

    companion object {
        const val PLUS_CLIENT = "org.telegram.plus"
    }

}