package com.p1neapplexpress.telegrec.audio

import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import java.io.File

class AudioProcessor {

    fun mergeAndConvert(first: File, second: File, result: File, onComplete: () -> Unit, onError: (throwable: Throwable) -> Unit) {
        val filterComplex = "-filter_complex \"[0][1]amerge=inputs=2,pan=stereo|FL<c0+c1|FR<c2+c3[a]\" -map \"[a]\""
        val ffmpegCommand =
            "-i ${first.absolutePath} -i ${second.absolutePath} $filterComplex ${result.absolutePath}"
        FFmpeg.executeAsync(ffmpegCommand) { _, p1 ->
            when (p1) {
                RETURN_CODE_SUCCESS -> onComplete.invoke()
                RETURN_CODE_CANCEL -> onError.invoke(RuntimeException("Async command execution cancelled by user."))
                else -> onError.invoke(RuntimeException("Async command execution failed. Code: $p1"))
            }
        }
    }

}