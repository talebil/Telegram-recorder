package com.p1neapplexpress.telegrec.ui.compose.recordings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.p1neapplexpress.telegrec.data.Recording
import com.p1neapplexpress.telegrec.ui.theme.DarkGray2
import com.p1neapplexpress.telegrec.util.PrettyDate


@Composable
fun RecordingListItem(
    recording: Recording,
    onClick: (recording: Recording) -> Unit
) {
    val inDarkTheme = isSystemInDarkTheme()

    Column {
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .clickable { onClick.invoke(recording) }
//            .alpha(if (enabled) 1f else 0.7f),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (inDarkTheme) DarkGray2 else Color.White)
                    .padding(16.dp),
            ) {
                Row {
                    Text(text = recording.callerID)
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                    Text(text = PrettyDate.from(recording.date, pattern = PrettyDate.SPACE_PATTERN))
                }
                Text(
                    text = "From: ${recording.app}",
                    modifier = Modifier.alpha(0.7f),
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))
    }
}
