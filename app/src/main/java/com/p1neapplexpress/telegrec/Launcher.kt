package com.p1neapplexpress.telegrec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.p1neapplexpress.telegrec.ui.compose.main.MainScreenView
import com.p1neapplexpress.telegrec.ui.compose.recordings.RecordingsScreenView
import com.p1neapplexpress.telegrec.ui.theme.TRecorderTheme


class Launcher : ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            TRecorderTheme {
                val pagerState = rememberPagerState(pageCount = { 2 })
                HorizontalPager(state = pagerState) { page ->
                    when (page) {
                        0 -> MainScreenView()
                        1 -> RecordingsScreenView()
                    }
                }

            }
        }
    }
}