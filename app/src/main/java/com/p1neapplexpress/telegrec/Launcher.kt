package com.p1neapplexpress.telegrec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.p1neapplexpress.telegrec.ui.compose.MainScreenView
import com.p1neapplexpress.telegrec.ui.theme.TRecorderTheme

class Launcher : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            TRecorderTheme {
                MainScreenView()
            }
        }
    }
}