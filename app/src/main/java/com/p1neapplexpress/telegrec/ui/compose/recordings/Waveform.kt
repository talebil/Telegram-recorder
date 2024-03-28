package com.p1neapplexpress.telegrec.ui.compose.recordings

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun AudioWaveView(
    samples: ShortArray
) {
    Row(
        modifier = Modifier
            .height(64.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val waveformPath = Path()

            val width = size.width
            val height = size.height

            val xStep = width / (samples.size - 1).toFloat()
            val yScale = height / Short.MAX_VALUE.toFloat()

            waveformPath.moveTo(0f, height / 2f)
    
            samples.forEachIndexed { index, sample ->
                val x = index * xStep
                val y = ((sample * 4) * yScale) + (height / 2f)
                waveformPath.lineTo(x, y)
            }

            drawPath(waveformPath, Color.White)
        }
    }
}